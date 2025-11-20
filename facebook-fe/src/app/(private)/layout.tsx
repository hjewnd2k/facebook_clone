import React from 'react';

import Protected from '@/contexts/Protected';

import Header from './components/Header';

const PrivateLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return (
    <Protected>
      <Header />
      <main className="bg-background mt-(--header-height)">{children}</main>
    </Protected>
  );
};

export default PrivateLayout;
