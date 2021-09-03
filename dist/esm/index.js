import { registerPlugin } from '@capacitor/core';
const Rfid = registerPlugin('Rfid', {
    web: () => import('./web').then(m => new m.RfidWeb()),
});
export * from './definitions';
export { Rfid };
//# sourceMappingURL=index.js.map