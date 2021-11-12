
const listOfOrderKeys = [
    "quantity",
    "side",
    "stock",
    "type",
    "targetPrice",
    "createdBy",
    "modifiedBy",
    "createdAt",
    "modifiedAt",
    "status",
    "isActive",
    "stockPrice"
];
const optionalKeys = [
    "stockPrice"
];
const mandatorilyNullKeysWhenCreated = [
    "exchangeId",
    "executeAt",
    "executedPrice",
    "quantityFilled",
    "targetPrice"
]
const nonStringKeys = [
    "clientId",
    "orderId",
    "targetPrice",
    "quantity",
    "modifiedBy",
    "createdBy",
    "isActive",
    "stockPrice"
]
const unprotectedRoutes = [
    "/",
    "/login",
    "/about",
    "/forget-password-email"
];
const inactivityTimeout = 3 * 60;
const invalidRefreshTokenResponse = "Request failed with status code 401";

export const UnprotectedRoutes = unprotectedRoutes;
export const OptionalKeys = optionalKeys;
export const ListOfOrderKeys = listOfOrderKeys;
export const InactivityTimeout = inactivityTimeout;
export const InvalidRefreshTokenResponse = invalidRefreshTokenResponse;
export const NonStringKeys = nonStringKeys;
export const MandatorilyNullKeysWhenCreated = mandatorilyNullKeysWhenCreated;
const exports = {
    OptionalKeys: optionalKeys,
    ListOfOrderKeys: listOfOrderKeys,
    UnprotectedRoutes: unprotectedRoutes,
    InactivityTimeout: inactivityTimeout,
    InvalidRefreshTokenResponse: invalidRefreshTokenResponse,
    NonStringKeys: nonStringKeys,
    MandatorilyNullKeysWhenCreated: mandatorilyNullKeysWhenCreated,
    CREATED : "CREATED",
    COMPLETED : "COMPLETED",
};
export default exports;
