import { registerPlugin } from '@capacitor/core';

import type { RfidPlugin } from './definitions';

const Rfid = registerPlugin<RfidPlugin>('Rfid', {
  web: () => import('./web').then(m => new m.RfidWeb()),
});

export * from './definitions';
export { Rfid };

