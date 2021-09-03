export interface RfidPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
