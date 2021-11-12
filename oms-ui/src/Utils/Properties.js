// Properties file to have all configurations in one place

const dev = {
  ORDER_SERVER_URL: 'http://localhost:9003/api/v1',
  USER_SERVER_URL: 'http://localhost:9002/api/v1',
  AUTH_SERVER_URL: 'http://localhost:9005/api/v1',
};

const prod = {
  ORDER_SERVER_URL: 'https://api.omssapient.com/api/v1',
  USER_SERVER_URL: 'https://api.omssapient.com/api/v1',
  AUTH_SERVER_URL: 'https://api.omssapient.com/api/v1/user',
};

const config = {
  
  SEND_ORDER_API: '/order/send',
  FETCH_CLIENT_API: '/client',
  CREATE_ORDER_API: '/order',
  USER_DETAILS_API: '/getDetails',
  USER_DETAILS_UPDATE_API: '/updateDetails',
  FORGOT_PASSWORD_API: '/forgot-password',
  UPDATE_PASSWORD_API: '/update-password',
  GET_BROKER_LIST:'/broker/getList',
  UPDATE_BROKER_LIST: '/broker/updateStatus/',
  GENERATE_TOKEN_API: '/generate-token',
  INVALIDATE_TOKEN_API: '/invalidate-token',
  AUTHENTICATE_TOKEN_API: '/authenticate',
  AUTHORIZE_TOKEN_API: '/authorize',
  
  REGISTER_API: '/register',

  ORDER_SEARCH_API: '/search-order',
  REFRESH_TOKEN_API: '/refresh-token',
  GET_CLIENT_BY_ID:'/client/',

  REQUEST_TIMEOUT: 10000,

  ...(process.env.REACT_APP_STAGE === 'production'? prod : dev)
}

export default config;