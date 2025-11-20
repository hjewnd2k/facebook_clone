// src/store/useUserStore.ts
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

export interface User {
  id: string;
  userId: string;
  username: string;
  email: string;
  displayName: string;
  profilePictureUrl: string;
  bio: string;
}

interface UserStore {
  user: User | null;
  isLoading: boolean;
  setUser: (user: User | null) => void;
  setLoading: (loading: boolean) => void;
}

export const useUserStore = create<UserStore>()(
  devtools((set) => ({
    user: null,
    isLoading: true,
    setUser: (user) => set({ user, isLoading: false }),
    setLoading: (isLoading) => set({ isLoading }),
  })),
);
