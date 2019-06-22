/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.terrypacker.acurite;

import java.util.Arrays;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.i18n.TranslatableException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataImage.DataPointRT;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;
import com.serotonin.m2m2.rt.dataImage.SetPointSource;
import com.serotonin.m2m2.rt.dataSource.PollingDataSource;


/**
 * @author Terry Packer
 *
 */
public class AcuriteDataSourceRT extends PollingDataSource<AcuriteDataSourceVO> {
    protected final Log LOG = LogFactory.getLog(AcuriteDataSourceRT.class);

    //Events that can be generated
    public static final int POLL_ABORTED_EVENT = 1;
    public static final int USB_DEVICE_NOT_FOUND_EVENT = 2;
    public static final int USB_COMMUNICATION_ERROR_EVENT = 3;
    public static final int DATA_SOURCE_ERROR = 4; //general error
    public static final int NO_SENSOR_FOUND_EVENT = 5; //Device is not connected to a sensor 
    public static final int INVALID_RESPONSE_EVENT = 6; //Corrupt or unknown response
    public static final int LOW_BATTERY_EVENT = 7;
    public static final int LOW_SIGNAL_EVENT = 8; //Signal too low to connect to remote sensors
    
    //Acurite Station USB id
    private final static short VENDOR = 9408;
    private final static short PRODUCT = 3;

    //Request Info
    private static final short VALUE = 0x0100;
    public static final short REPORT_1 = 1;
    public static final short REPORT_2 = 2;
    private final static byte REQUEST = 0x01;
    private final static short INDEX = 0;

    //Wind direction from true north
    private static final double[] DEGREES = {315.0, 247.5, 292.5, 270.0, 337.5, 225.0, 0.0, 202.5, 67.5, 135, 90.0, 112.5, 45.0, 157.5, 22.5, 180.0  };
    
    //Device used during runtime
    private UsbDevice device;
    
    public AcuriteDataSourceRT(AcuriteDataSourceVO vo) {
        super(vo);
    }

    @Override
    public void initialize() {
        try {
            System.setProperty("javax.usb.services", "org.usb4java.javax.Services");
            UsbServices services = UsbHostManager.getUsbServices();
            UsbHub rootHub  = services.getRootUsbHub();
            device = findDevice(rootHub, VENDOR, PRODUCT);
            //Todo if we want device info: device.getProductString();
        } catch(DeviceNotFoundException e) {
            raiseEvent(USB_DEVICE_NOT_FOUND_EVENT, Common.timer.currentTimeMillis(), true, e.getTranslatableMessage());
        }catch(Exception e) {
            raiseEvent(DATA_SOURCE_ERROR, Common.timer.currentTimeMillis(), true, new TranslatableMessage("acurite.event.genericError", e.getMessage()));
        }
    }
    
    @Override
    protected void doPoll(long time) {
        if(device == null)
            initialize();
        if(device == null)
            return;
        try {
            //Request report 1
            Report1Response r1 = null;
            try{
                short request = VALUE + REPORT_1;
                byte[] response = requestData(request);
                r1 = processReport1Response(response);
                returnToNormal(NO_SENSOR_FOUND_EVENT, time);
                returnToNormal(LOW_SIGNAL_EVENT, time);
            } catch(InvalidResponseFormatException e) {
                raiseEvent(INVALID_RESPONSE_EVENT, time, false, e.getTranslatableMessage());
            } catch(NoSensorsException e) {
                raiseEvent(NO_SENSOR_FOUND_EVENT, time, true, e.getTranslatableMessage());
            } catch(LowSignalException e) {
                raiseEvent(LOW_SIGNAL_EVENT, time, true, e.getTranslatableMessage());
            }
            
            //Request report 2
            Report2Response r2 = null;
            try {
                short request = VALUE + REPORT_2;
                byte[] response = requestData(request);
                r2 = processReport2Response(response);
            } catch(InvalidResponseFormatException e) {
                raiseEvent(INVALID_RESPONSE_EVENT, time, false, e.getTranslatableMessage());
            }
            
            for(DataPointRT rt : dataPoints) {
                AcuritePointLocatorRT plrt = rt.getPointLocator();
                AcuritePointLocatorVO plvo = (AcuritePointLocatorVO)plrt.getVo();

                switch(plrt.getReportType()) {
                    case REPORT_1:
                        if(r1 == null) {
                            rt.setAttribute(ATTR_UNRELIABLE_KEY, true);
                            break;
                        }else
                            rt.setAttribute(ATTR_UNRELIABLE_KEY, false);
                        switch(plvo.getMeasurement()) {
                            case WIND_SPEED:
                                rt.setPointValue(new PointValueTime(r1.windSpeed, time), null);
                                break;
                            case WIND_DIRECTION:
                                if(r1.flavor == 1)
                                    rt.setPointValue(new PointValueTime(r1.windDirection, time), null);
                                break;
                            case RAIN:
                                if(r1.flavor == 1)
                                    rt.setPointValue(new PointValueTime(r1.totalRain, time), null);
                                break;
                            case TEMPERATURE_OUTSIDE:
                                if(r1.flavor == 8)
                                    rt.setPointValue(new PointValueTime(r1.temperature, time), null);
                                break;
                            case HUMIDITY:
                                if(r1.flavor == 8)
                                    rt.setPointValue(new PointValueTime(r1.humidity, time), null);
                                break;
                            default:
                        }
                        break;
                    case REPORT_2:
                        if(r2 == null) {
                            rt.setAttribute(ATTR_UNRELIABLE_KEY, true);
                            break;
                        }else
                            rt.setAttribute(ATTR_UNRELIABLE_KEY, false);
                        switch(plvo.getMeasurement()) {
                            case PRESSURE:
                                rt.setPointValue(new PointValueTime(r2.pressure, time), null);
                                break;
                            case TEMPERATURE_INSIDE:
                                rt.setPointValue(new PointValueTime(r2.temperature, time), null);
                                break;
                            default:
                                break;
                            
                        }
                        break;
                }
            }
            
        } catch(NoUsbDataRecievedException | UsbRequestException e) {
            raiseEvent(USB_COMMUNICATION_ERROR_EVENT, time, true, e.getTranslatableMessage());
        } catch(Exception e) {
            raiseEvent(DATA_SOURCE_ERROR, time, true, new TranslatableMessage("acurite.event.genericError", e.getMessage()));
        }
    }

    @Override
    public void setPointValueImpl(DataPointRT dataPoint, PointValueTime valueTime,
            SetPointSource source) {
        //No-op, can't set anything
    }
    
    private byte[] requestData(short request) throws TranslatableException {
        byte[] data = new byte[50];
        UsbControlIrp irp = device.createUsbControlIrp((byte) (UsbConst.REQUESTTYPE_TYPE_CLASS
                | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE | UsbConst.ENDPOINT_DIRECTION_IN), REQUEST, request, INDEX);
        irp.setData(data);
        irp.setLength(50);
        if (data == null || data.length == 0) {
            throw new NoUsbDataRecievedException();
        }
        try {
            device.syncSubmit(irp);
        } catch(UsbException e) {
            throw new UsbRequestException(e);
        } catch(UsbDisconnectedException e) {
            device = null;
            throw new DeviceNotFoundException();
        }
        if (!irp.isComplete()) {
            throw new NoUsbDataRecievedException();
        }
        if(irp.isUsbException()) {
            throw new UsbRequestException(irp.getUsbException());
        }
        return Arrays.copyOfRange(data, 0, irp.getActualLength());
    }
    
    /**
     * Find the USB device by searching the hub(s)
     * @param hub
     * @param vendorId
     * @param productId
     * @return
     * @throws DeviceNotFoundException
     */
    private UsbDevice findDevice(UsbHub hub, short vendorId, short productId) throws DeviceNotFoundException{
        UsbDevice found = findDeviceRecursively(hub, vendorId, productId);
        if(found != null) {
            returnToNormal(USB_DEVICE_NOT_FOUND_EVENT, Common.timer.currentTimeMillis());
            return found;
        }
        throw new DeviceNotFoundException();
    }
    
    @SuppressWarnings("unchecked")
    private UsbDevice findDeviceRecursively(UsbHub hub, short vendorId, short productId) {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
                return device;
            }
            if (device.isUsbHub()) {
                device = findDeviceRecursively((UsbHub) device, vendorId, productId);
                if (device != null) {
                    return device;
                }
            }
        }
        return null;
    }

    /**
     * Get outdoor sensor data, can be of 2 flavors:
     * 
     * 1: wind speed, wind direction, rain counter
     * 8: wind speed, outside temperature, humidity
     * 
     * @param response
     * @return
     */
    private Report1Response processReport1Response(byte[] data) throws TranslatableException {

        Report1Response weatherData = new Report1Response();
        
        //Confirm structure
        if(data.length != 10 || data[0] != 0x01 || (data[9] != (byte)0xff && data[9] != 0x00)) {
            LOG.error("Invalid Report 1 response structure");
            throw new InvalidResponseFormatException(REPORT_1);
        }
        
        //Check if there is a sensor
        if((data[1] & 0x0f) == 0x0f && data[3] == 0xff) {
            throw new NoSensorsException();
        }
        
        //int messageTypeId = data[0];
        //int channel = data[1] & 0xF0; //0xC = A, 0x8 = B 0x0 = C
        //1 &0x0F sensorId high
        //int sensorId = data[1] & 0x0F;
        //sensorId = sensorId << 8;
        //2 sensorId low
        //sensorId = sensorId | data[2];
        
        //3 &0x0F message flavor
        weatherData.flavor = data[3] & 0x0F; //type 1 is windSpeed, windDir, rain
        if(weatherData.flavor != 1 && weatherData.flavor != 8) {
            LOG.error("Invalid Report1 Flavor");
            throw new InvalidResponseFormatException(REPORT_1);
        }
        
        //Confirm we can get valid Rssi
        if ((data[8] & 0x0f) < 0 || (data[8] & 0x0f) > 3) {
            LOG.error("Invalid RSSI");
            throw new InvalidResponseFormatException(REPORT_1);
        }
        
        //Check battery
        if(((data[3] & 0xf0) >> 4) != 0x07) {
            raiseEvent(LOW_BATTERY_EVENT, Common.timer.currentTimeMillis(), true, new TranslatableMessage("acurite.event.lowBattery"));
        }else {
            returnToNormal(LOW_BATTERY_EVENT, Common.timer.currentTimeMillis());
        }
        
        int rssi = getRssi(data);
        if(rssi == 0) {
            //No comms to outdoor sensor
            throw new LowSignalException();
        }
        
        if (weatherData.flavor == 1) {
            weatherData.windSpeed = getWindSpeed(data);
            weatherData.windDirection = getWindDirection(data);
            weatherData.totalRain = getRain(data);

        }else if (weatherData.flavor == 8) {
            weatherData.windSpeed = getWindSpeed(data);
            weatherData.temperature = getTemperature(data);
            weatherData.humidity = getHumidity(data);
        }
        return weatherData;
    }
    
    /**
     * @return Wind speed in kh/h
     */
    private double getWindSpeed(byte[] data) {
        int leftSide = (data[4] & 0x1f) << 3;
        int rightSide = (data[5] & 0x70) >> 4;
        int temp = leftSide | rightSide;
        if (temp == 0)
            return 0.0;
        return 0.8278 * temp + 1.0d;
    }

    /**
     * Degress from true north
     * @param data
     * @return
     */
    private double getWindDirection(byte[] data) {
        return DEGREES[data[5] & 0x0f];
    }

    /**
     * Humidity 0 - 100
     * @param data
     * @return
     */
    private double getHumidity(byte[] data) {
        return (double)(data[7] & 0x7f);
    }

    /**
     * Get temperature in Degrees C
     * @param data
     * @return
     */
    private double getTemperature(byte[] data) {
        // This item spans bytes, have to reconstruct it
        int leftSide = (data[5] & 0x0f) << 7;
        int rightSide = data[6] & 0x7f;
        double combined = leftSide | rightSide;
        //For Deg F return (combined - 400) / 10.0;
        return combined / 18.0d - 40.0d;
    }

    /**
     * Return total rain ever collected in inches
     * each bucket tip is 0.01 inches
     * @param data
     * @return
     */
    private double getRain(byte[] data) {
        return (((data[6] & 0x3f) << 7) | (data[7] & 0x7f)) * 0.00993797; //Should be 0.01 but not on my system?;
    }
    
    
    /**
     * Get Signal quality
     * signal strength goes from 0 to 3, inclusive
     * according to nincehelser, this is a measure of the number of failed
     * sensor queries, not the actual RF signal strength
     * @param data
     * @return
     */
    private int getRssi(byte[] data) {
        return data[8] & 0x0f;
    }
    
    private Report2Response processReport2Response(byte[] data) throws TranslatableException {
        //Confirm structure
        if(data.length != 25 || data[0] != 0x02) {
            LOG.error("Invalid Report 2 response structure");
            throw new InvalidResponseFormatException(REPORT_1);
        }
        boolean useConstants = true; //TODO make setting
        boolean ignoreBounds = false;
        
        //First get the constants from the message
        int data3 = convertToUnsignedInt(data[3]);
        int data4 = convertToUnsignedInt(data[4]);
        int data5 = convertToUnsignedInt(data[5]);
        int data6 = convertToUnsignedInt(data[6]);
        int data7 = convertToUnsignedInt(data[7]);
        int data8 = convertToUnsignedInt(data[8]);
        int data9 = convertToUnsignedInt(data[9]);
        int data10 = convertToUnsignedInt(data[10]);
        int data11 = convertToUnsignedInt(data[11]);
        int data12 = convertToUnsignedInt(data[12]);
        int data13 = convertToUnsignedInt(data[13]);
        int data14 = convertToUnsignedInt(data[14]);
        int data15 = convertToUnsignedInt(data[15]);
        int data16 = convertToUnsignedInt(data[16]);
        int data17 = convertToUnsignedInt(data[17]);
        int data18 = convertToUnsignedInt(data[18]);
        int data19 = convertToUnsignedInt(data[19]);
        int data20 = convertToUnsignedInt(data[20]);
        int data21 = convertToUnsignedInt(data[21]);
        int data22 = convertToUnsignedInt(data[22]);
        int data23 = convertToUnsignedInt(data[23]);
        int data24 = convertToUnsignedInt(data[24]);
        
        int c1 = (data3 << 8) + data4; //Sensitivity constant
        int c2 = (data5 << 8) + data6; //offset coefficient
        int c3 = (data7 << 8) + data8; //temperature coefficient of sensitivity
        int c4 = (data9 << 8) + data10; //temperature coefficient of offset
        int c5 = (data11 << 8) + data12; //reference temperature
        int c6 = (data13 << 8) + data14; //temperature coefficient of temperature
        int c7 = (data15 << 8) + data16; //offset fine tuning
        int a = data17; //Sensor specific
        int b = data18; //Sensor specific
        int c = data19; //Sensor specific
        int d = data20; //Sensor specific
        
        Report2Response report2 = new Report2Response();
        
        if(useConstants) {
            //Determine pressure sensor type
            if ((c1 == 0x8000) && (c2 == 0x0) && (c3 ==0x0)  && (c4 == 0x0400)
                    && (c5 == 0x1000) && (c6 == 0x0) && (c7 == 0x0960)
                    && (a == 0x1) && (b == 0x1) && (c == 0x1) && (d == 0x1)) {
                //this is a MS5607 sensor, typical in 02032 consoles
                int d2 = ((data21 & 0x0f) << 8) + data22;
                if (d2 >= 0x0800)
                    d2 -= 0x1000;
                int d1 = (data23 << 8) + data24;
                // apparently the new (2015) acurite software uses this function, which
                // is quite close to andrew daviel's reverse engineered function of:
                //    p = 0.062585727 * d1 - 209.6211
                //    t = 25.0 + 0.05 * d2
                report2.pressure = (double)d1 / 16.0d - 208d; //mbar
                report2.temperature = 25.0d + 0.05d * (double)d2; //C
                //temperature = (temperature * 9d/5d) + 32d; //F
                //pressure = pressure * 0.750062d; //mmHg
                //pressure = pressure / 33.864d; //inHg
            }else if(((0x100 <= c1 && c1 <= 0xffff) &&
                    (0x0 <= c2 && c2 <= 0x1fff) &&
                    (0x0 <= c3 && c3 <= 0x400 ) &&
                    (0x0 <= c4 && c4 <= 0x1000 ) &&
                    (0x1000 <= c5 && c5 <= 0xffff ) &&
                    (0x0 <= c6 && c6 <= 0x4000 ) &&
                    (0x960 <= c7 && c7 <= 0xa28 ) &&
                    (0x01 <= a && a <= 0x3f) && 
                    (0x01 <= b && b <= 0x3f) &&
                    (0x01 <= c && c <= 0x0f) && 
                    (0x01 <= d && d <= 0x0f)) || ignoreBounds) {
                //this is a HP038 sensor.  some consoles return values outside the
                //specified limits, but their data still seem to be ok.  if the
                //ignore_bounds flag is set, then permit values for A, B, C, or D
                //that are out of bounds, but enforce constraints on the other
                //constants C1-C7
                int d2 = (data21 << 8) + data22;
                int d1 = (data23 << 8) + data24;
                double dut;
                if (d2 >= c5)
                    dut = d2 - c5 - ((d2-c5)/128) * ((d2-c5)/128) * a / (2<<(c-1));
                else
                    dut = d2 - c5 - ((d2-c5)/128) * ((d2-c5)/128) * b / (2<<(c-1));
                double off = 4 * (c2 + (c4 - 1024) * dut / 16384);
                double sens = c1 + c3 * dut / 1024;
                double x = sens * ((double)d1 - 7168d) / 16384 - off;
                report2.pressure = 0.1d * (x * 10d / 32d + (double)c7); //mbar 
                report2.temperature = 0.1d * (250d + dut * (double)c6 / 65536d - dut / (double)(2<<(d-1))); //degrees C
                //temperature = (temperature * 9d/5d) + 32d; //F
                //pressure = pressure * 0.750062d; //mmHg
                //pressure = pressure / 33.864d; //inHg
            }else {
                LOG.error("Unknown Report 2 Constants");
                throw new InvalidResponseFormatException(REPORT_2);
            }
        }else {
            // use a linear approximation for pressure and temperature
            int d2 = ((data21 & 0x0f) << 8) + data22;
            if (d2 >= 0x0800)
                d2 -= 0x1000;
            int d1 = (data23 << 8) + data24;
            // apparently the new (2015) acurite software uses this function, which
            // is quite close to andrew daviel's reverse engineered function of:
            //    p = 0.062585727 * d1 - 209.6211
            //    t = 25.0 + 0.05 * d2
            report2.pressure = (double)d1 / 16.0d - 208d; //mbar
            report2.temperature = 25.0 + 0.05 * (double)d2; //C
            //temperature = (temperature * 9d/5d) + 32d; //F
            //pressure = pressure * 0.750062d; //mmHg
            //pressure = pressure / 33.864d; //inHg
        }
        return report2;
    }
    
    private int convertToUnsignedInt(byte b) {
        int i = (int)b;
        return 0x000000FF & i;
    }
    
    class Report1Response {
        int flavor;
        double windSpeed; // In km/h
        double temperature;  //Deg C
        double windDirection; //Deg from true north
        double totalRain; //Appears to be an upward counting value of total rain recorded
        double humidity; // 0 to 100 
    }

    class Report2Response {
        double pressure; //mbar
        double temperature; //Deg C
    }

    /**
     * Indicate the station was not found on a USB port
     * @author Terry Packer
     *
     */
    class DeviceNotFoundException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public DeviceNotFoundException(){
            super(new TranslatableMessage("acurite.event.deviceNotFound"));
        }
    }
    
    /**
     * No data received from request
     * @author Terry Packer
     *
     */
    class NoUsbDataRecievedException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public NoUsbDataRecievedException() {
            super(new TranslatableMessage("acurite.event.usbCommsNoDataReceived"));
        }
    }
    
    /**
     * Usb comms exception
     * @author Terry Packer
     *
     */
    class UsbRequestException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public UsbRequestException(UsbException e) {
            super(new TranslatableMessage("acurite.event.usbCommsNoDataReceived", e == null ? "" : e.getMessage()));
        }
    }
    
    /**
     * Response is unsupported as of time of writing code
     * @author Terry Packer
     *
     */
    class InvalidResponseFormatException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public InvalidResponseFormatException(int reportType) {
            super(new TranslatableMessage("acurite.event.invalidResponseFormat", reportType == REPORT_1 ? "Report1" : "Report2"));
        }
    }
    

    /**
     * No remote sensor found
     * @author Terry Packer
     *
     */
    class NoSensorsException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public NoSensorsException() {
            super(new TranslatableMessage("acurite.event.noSensorsFound"));
        }
    }
    
    class LowSignalException extends TranslatableException {
        private static final long serialVersionUID = 1L;
        public LowSignalException() {
            super(new TranslatableMessage("acurite.event.lowSignal"));
        }
    }
}
