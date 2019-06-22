/**
 * Copyright (C) 2019  Infinite Automation Software. All rights reserved.
 */
package com.terrypacker.acurite;

import com.infiniteautomation.mango.util.ReverseEnum;
import com.infiniteautomation.mango.util.ReverseEnumMap;
import com.serotonin.m2m2.i18n.TranslatableMessage;

/**
 * @author Terry Packer
 *
 */
public enum WeatherMeasurement implements ReverseEnum<Integer> {
    
    TEMPERATURE_INSIDE(1, new TranslatableMessage("acurite.insideTemperature")),
    TEMPERATURE_OUTSIDE(2, new TranslatableMessage("acurite.outsideTemperature")),
    WIND_SPEED(3, new TranslatableMessage("acurite.windSpeed")),
    WIND_DIRECTION(4, new TranslatableMessage("acurite.windDirection")),
    RAIN(5, new TranslatableMessage("acurite.rain")),
    HUMIDITY(6, new TranslatableMessage("acurite.humidity")),
    PRESSURE(7, new TranslatableMessage("acurite.pressure"));
    
    private static ReverseEnumMap<Integer, WeatherMeasurement> map = new ReverseEnumMap<>(WeatherMeasurement.class);
    private final int value;
    private final TranslatableMessage description;
    
    private WeatherMeasurement(int value, TranslatableMessage description) {
        this.value = value;
        this.description = description;
    }
    
    public TranslatableMessage getDescription() {
        return description;
    }
    
    @Override
    public Integer value() {
        return this.value;
    }
    
    public static WeatherMeasurement fromValue(Integer value) {
        return map.get(value);
    }
    
    public static WeatherMeasurement fromName(String name) {
        return Enum.valueOf(WeatherMeasurement.class, name);
    }
}
