/**
 * @copyright 2019 {@link http://terrypacker.com|Terry Packer} All rights reserved.
 * @author Terry Packer
 */

import acuriteDataSourceEditor from './acuriteDataSourceEditor.html';

class AcuriteDataSourceEditorController {
    static get $$ngIsClass() { return true; }
    static get $inject() { return []; }
    
    constructor() {
    }
    
    $onChanges(changes) {
    }
}

export default {
    template: acuriteDataSourceEditor,
    controller: AcuriteDataSourceEditorController,
    bindings: {
        dataSource: '<source'
    }
};