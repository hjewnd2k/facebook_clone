import React from 'react';

import NewPost from './NewPost';
import Posts from './Posts';
import Stories from './Stories';

const HomeModule = () => {
  return (
    <div className="h-[calc(100dvh-var(--header-height))]">
      <div className="mx-auto w-full max-w-170 pt-4">
        <NewPost />
        <Stories />
        <Posts />
      </div>
    </div>
  );
};

export default HomeModule;
