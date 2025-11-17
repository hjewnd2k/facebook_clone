import Protected from "@/contexts/Protected";
import React from "react";

const PrivateLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return <Protected>{children}</Protected>;
};

export default PrivateLayout;
