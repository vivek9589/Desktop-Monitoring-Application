// config.js
export const CONFIG = {
  BASE_URL: "http://localhost:9090",   // change to prod URL when building
  API_ACTIVE_TAB: "/api/active-tab",
  LOG_LEVEL: "debug"
};

// Derived full URL
export const AGENT_URL = `${CONFIG.BASE_URL}${CONFIG.API_ACTIVE_TAB}`;