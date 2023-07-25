import { configureStore, combineReducers } from "@reduxjs/toolkit";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage"; // default: localStorage

import themeReducer from "./DarkMode/themeSlice";
import notificationReducer from "./notification/notificationSlice";
import commentReducer from "./comment/commentSlice";
import userReducer from "./users/userSlice";
import paymentReducer from "./payment/paymentSlice";
import loggedInReducer from "./loggedUser/loggedUser";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["loggedUser"], // only loggedUser will be persisted, add other reducer names if needed
};

const rootReducer = combineReducers({
  theme: themeReducer,
  notification: notificationReducer,
  comment: commentReducer,
  user: userReducer,
  payment: paymentReducer,
  loggedUser: loggedInReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
