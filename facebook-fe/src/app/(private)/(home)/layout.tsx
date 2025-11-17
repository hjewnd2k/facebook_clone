import React from "react";
import Header from "./components/Header";

const HomeLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return (
    <main>
      <Header />
      {children}
    </main>
  );
};

export default HomeLayout;
