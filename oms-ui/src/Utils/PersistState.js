export const persistState = (storageKey, state) => {
  localStorage.setItem(storageKey, JSON.stringify(state));
};

export const getInitialState = (storageKey) => {
  const savedState = localStorage.getItem(storageKey);
  try {
    if (!savedState) {
      return null;
    }
    return JSON.parse(savedState ?? "{}");
  } catch (e) {
    console.error("Error loading state :" + storageKey);
    return null;
  }
};