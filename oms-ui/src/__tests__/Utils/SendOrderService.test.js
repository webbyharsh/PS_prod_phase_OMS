import SendOrderService from "../../Utils/SendOrderService";
import OmsAxios from "../../Utils/OmsAxios";

describe('Success send order action tests', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });
    
    let order = {
        quantity: 342,
        side: 'buy',
        stock: 'tcs',
        type: 'market',
        targetPrice: null,
        createdBy: 1,
        modifiedBy: 1,
        createdAt: '22/22/22',
        modifiedAt: '22/22/22',
        clientId: 1,
        status: 'CREATED',
        isActive: true
    };
    let sendOrderServiceInstance = new SendOrderService(order);

    it('should validate order', () => {
        expect(sendOrderServiceInstance.isOrderValid).toBeTruthy();
    });

    it('should be available to send', async () => {
        // if server says it is authorized
        OmsAxios.get = jest.fn(() => {
            return Promise.resolve({
                data: { listRoles: ["ROLE_ADMIN"] },
                status: 200
            });
        })
        let isAvailable = await sendOrderServiceInstance.isAvailableToSend();
        expect(isAvailable).toBeTruthy();
    });

    it('should return non-null after sending', async () => {
        let mock = jest.fn(() => {
            return Promise.resolve({
                data: { listRoles: ["ROLE_ADMIN"] },
                status: 200
            });
        })
        OmsAxios.put = mock;
        OmsAxios.get = mock;
        let isSent = await sendOrderServiceInstance.send();
        expect(isSent).not.toBeNull();
    });
});

describe('Failure send order action tests', () => {
    let invalidOrder = {
        quantity: 3421,
        side: 'buy',
        stock: 'tcs',
        targetPrice: null,
        createdBy: 1,
        modifiedBy: 1,
        createdAt: '22/22/22',
        modifiedAt: new Date(),
        clientId: 1,
        isActive: true
    };
    let validOrder = {
        quantity: 342,
        side: 'buy',
        stock: 'tcs',
        type: 'market',
        targetPrice: null,
        createdBy: 1,
        modifiedBy: 1,
        createdAt: '22/22/22',
        modifiedAt: '22/22/22',
        status: 'CREATED',
        clientId: 1,
        isActive: true
    };

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should validate order', () => {
        let sendOrderServiceInstance = new SendOrderService(invalidOrder);
        expect(sendOrderServiceInstance.isOrderValid).toBeFalsy();
    });

    it('should be available to send', async () => {
        let sendOrderServiceInstance = new SendOrderService(validOrder);
        // if server says it is authorized
        OmsAxios.get = jest.fn(() => {
            return Promise.resolve({
                data: {
                    listRoles: ["ROLE_Dummy"]
                },
            });
        })
        let isAvailable = await sendOrderServiceInstance.isAvailableToSend();
        expect(isAvailable).toBeFalsy();
    });

    it('should error out trying to connect', async () => {
        let sendOrderServiceInstance = new SendOrderService(validOrder);
        let isAvailable = await sendOrderServiceInstance.isAvailableToSend();
        expect(isAvailable).toBeFalsy();
        expect(sendOrderServiceInstance.error.status)
            .toBe(sendOrderServiceInstance.STATUS_SERVER_UNREACHABLE);
    });

    it('should return null after sending', async () => {
        let sendOrderServiceInstance = new SendOrderService(validOrder);
        let mock = jest.fn(() => {
            return Promise.resolve({
                data: { listRoles: ["ROLE_Dummy"] },
                status: 404
            });
        });
        OmsAxios.get = mock
        OmsAxios.post = mock
        let isSent = await sendOrderServiceInstance.send();
        expect(isSent).toBeNull();
        expect(sendOrderServiceInstance.error.status)
            .toBe(sendOrderServiceInstance.STATUS_UNAUTHORIZED_USER);
    });

    it('should error out after sending', async () => {
        let sendOrderServiceInstance = new SendOrderService(validOrder);
        console.error = jest.fn();
        let mock = jest.fn(() => {
            throw Error('Network Error');
        }).mockImplementationOnce(() => {
            return Promise.resolve({
                data: { listRoles: ["ROLE_ADMIN"] },
                status: 200
            });
        });
        OmsAxios.get = mock
        OmsAxios.put = mock
        let isSent;
        try {
            isSent = await sendOrderServiceInstance.send();
        } catch (err) {
            expect(err.message).toBe("Network Error");
            expect(sendOrderServiceInstance.error.message).toBe("Unknown error occurred");
            expect(sendOrderServiceInstance.error.status).toBeNull();
            expect(typeof isSent).toBe("undefined");
        }
    });
});