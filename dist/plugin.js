var capacitorRfid = (function (exports, core) {
    'use strict';

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

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
