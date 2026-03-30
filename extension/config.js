// config.js
export const CONFIG = {
  AGENT_URL: process.env.AGENT_URL || "http://localhost:9090/api/active-tab",
  LOG_LEVEL: "debug" // can be "info", "warn", "error"
};