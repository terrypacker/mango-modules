/**
 * @copyright 2019 {@link http://terrypacker.com|Terry Packer} All rights reserved.
 * @author Terry Packer
 */

import angular from 'angular';
import acuriteDataSourceEditor from './components/acuriteDataSourceEditor/acuriteDataSourceEditor';
import acuriteDataPointEditor from './components/acuriteDataPointEditor/acuriteDataPointEditor';
import dsHelpTemplate from './help/dsHelp.html';
import dpHelpTemplate from './help/dpHelp.html';

const acuriteModule = angular.module('maAcuriteModule', ['maUiApp'])
.component('maAcuriteDataSourceEditor', acuriteDataSourceEditor)
.component('maAcuriteDataPointEditor', acuriteDataPointEditor)
.config(['maDataSourceProvider', 'maPointProvider', 'maUiMenuProvider', function(maDataSourceProvider, maPointProvider, maUiMenuProvider) {
    maDataSourceProvider.registerType({
        type: 'ACURITE',
        description: 'acurite.description',
        template: `<ma-acurite-data-source-editor data-source="$ctrl.dataSource"></ma-acurite-data-source-editor>`,
        polling: true,
        defaultDataSource: {
            name: '',
            enabled: false,
            descriptionKey: 'acurite.description',
            pollPeriod: {
                periods: 1,
                type: 'MINUTES'
            },
            editPermission: ['superadmin'],
            purgeSettings: {
                override: false,
                frequency: {
                    periods: 1,
                    type: 'YEARS'
                }
            },
            eventAlarmLevels: [
                {eventType: 'POLL_ABORTED', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'event.ds.pollAborted'},
                {eventType: 'USB_DEVICE_NOT_FOUND_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.deviceNotFound'},
                {eventType: 'USB_COMMUNICATION_ERROR_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.usbCommsNoDataReceived'},
                {eventType: 'DATA_SOURCE_ERROR', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.usbException'},
                {eventType: 'NO_SENSOR_FOUND_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.invalidResponseFormat'},
                {eventType: 'INVALID_RESPONSE_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.noSensorsFound'},
                {eventType: 'LOW_BATTERY_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.lowSignal'},
                {eventType: 'LOW_SIGNAL_EVENT', level: 'URGENT', duplicateHandling: 'IGNORE', descriptionKey: 'acurite.event.description.lowBattery'},
                ],
            quantize: false,
            useCron: false,
            modelType: 'ACURITE'
        },
        defaultDataPoint: {
            dataSourceTypeName: 'ACURITE',
            pointLocator: {
                measurement: 'TEMPERATURE_OUTSIDE',
                dataType: 'NUMERIC',
                modelType: 'PL.ACURITE',
                settable: false
            }
        },
        bulkEditorColumns: [
            {name: 'pointLocator.measurement', label: 'acurite.measurement', selectedByDefault: true}
        ]
    });

    maPointProvider.registerType({
        type: 'ACURITE',
        description: 'acurite.point.description',
        template: `<ma-acurite-data-point-editor data-point="$ctrl.dataPoint"></ma-acurite-data-point-editor>`
    });
    maUiMenuProvider.registerMenuItems([
        {
            name: 'ui.help.acuriteDataSource',
            url: '/acurite-data-source',
            menuTr: 'acurite.description',
            template: dsHelpTemplate
        },
        {
            name: 'ui.help.acuriteDataPoint',
            url: '/acurite-data-point',
            menuTr: 'acurite.point.description',
            template: dpHelpTemplate
        }
    ]);
}]);

export default acuriteModule;