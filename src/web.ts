import { WebPlugin } from '@capacitor/core';

import type { RfidPlugin } from './definitions';

export class RfidWeb extends WebPlugin implements RfidPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('checkPermission', options);
    return options;
  }
}
