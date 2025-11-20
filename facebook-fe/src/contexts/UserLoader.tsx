// components/auth/UserLoader.tsx
'use client';

import { useKeycloak } from '@react-keycloak/web';
import { useEffect } from 'react';

import { API_ROUTES } from '@/constants';
import api from '@/libs/api';
import { useUserStore } from '@/stores/user.store';
import { IUserResponse } from '@/types';

export default function UserLoader() {
  const { keycloak, initialized } = useKeycloak();
  const { setUser, user } = useUserStore();

  useEffect(() => {
    if (!initialized || !keycloak.authenticated || !keycloak.token) {
      if (!keycloak.authenticated) setUser(null);
      return;
    }

    if (user) return; // đã có rồi

    api
      .get<IUserResponse>(API_ROUTES.USERS.ME)
      .then((res) => {
        console.log(res);
        setUser(res.result);
      })
      .catch((err) => {
        console.error(err);
        // if (err.response?.status === 401) keycloak.logout();
      });
  }, [initialized, keycloak.authenticated, keycloak.token, user]);

  return null; // component không render gì
}
