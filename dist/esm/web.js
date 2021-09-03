import { WebPlugin } from '@capacitor/core';
export class RfidWeb extends WebPlugin {
    async echo(options) {
        console.log('checkPermission', options);
        return options;
    }
}
//# sourceMappingURL=web.js.map