import { CONFIG } from "./config.js";
import { log } from "./utils/logger.js";

chrome.tabs.onActivated.addListener(async (activeInfo) => {
  try {
    const tab = await chrome.tabs.get(activeInfo.tabId);
    sendTabData(tab);
  } catch (err) {
    log("error", "Error onActivated", err);
  }
});

chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {
  if (changeInfo.status === "complete" && tab.active) {
    sendTabData(tab);
  }
});

async function sendTabData(tab) {
  if (!tab?.url) return;

  const payload = {
    url: tab.url,
    title: tab.title,
    timestamp: Date.now()
  };

  try {
    const response = await fetch(CONFIG.AGENT_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    log("info", "Sent tab data", payload);
  } catch (err) {
    log("error", "Failed to send tab data", err);
  }
}