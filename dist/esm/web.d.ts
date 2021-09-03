import { WebPlugin } from '@capacitor/core';
import type { RfidPlugin } from './definitions';
export declare class RfidWeb extends WebPlugin implements RfidPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
