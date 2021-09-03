'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const Rfid = core.registerPlugin('Rfid', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.RfidWeb()),
});

class RfidWeb extends core.WebPlugin {
    async echo(options) {
        console.log('checkPermission', options);
        return options;
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    RfidWeb: RfidWeb
});

exports.Rfid = Rfid;
//# sourceMappingURL=plugin.cjs.js.map
