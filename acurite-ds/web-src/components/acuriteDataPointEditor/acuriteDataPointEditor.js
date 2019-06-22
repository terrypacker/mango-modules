/**
 * @copyright 2019 {@link http://terrypacker.com|Terry Packer} All rights reserved.
 * @author Terry Packer
 */

import acuriteDataPointEditor from './acuriteDataPointEditor.html';

class AcuriteDataPointEditorController {
    static get $$ngIsClass() { return true; }
    static get $inject() { return ['maPoint']; }
    
    constructor(maPoint) {
        
    }
    
}

export default {
    template: acuriteDataPointEditor,
    controller: AcuriteDataPointEditorController,
    bindings: {
        dataPoint: '<point'
    },
    require: {
        pointEditor: '^maDataPointEditor'
    }
};    