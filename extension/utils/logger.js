// utils/logger.js
import { CONFIG } from "../config.js";

export function log(level, message, data) {
  if (shouldLog(level)) {
    const prefix = `[ActivityTracker][${level.toUpperCase()}]`;
    if (data) {
      console[level](`${prefix} ${message}`, data);
    } else {
      console[level](`${prefix} ${message}`);
    }
  }
}

function shouldLog(level) {
  const levels = ["debug", "info", "warn", "error"];
  return levels.indexOf(level) >= levels.indexOf(CONFIG.LOG_LEVEL);
}