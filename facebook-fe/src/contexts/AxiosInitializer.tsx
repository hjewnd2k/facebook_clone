"use client";

import { useAxiosAuth } from "@/libs/axios";
import { useEffect } from "react";

export default function AxiosInitializer() {
  const api = useAxiosAuth();

  // Optional: Log để debug
  useEffect(() => {
    console.log("Axios + Keycloak initialized");
  }, [api]);

  return null;
}
