'use client';
import React, { useMemo } from 'react';

import { useGetNewFeed } from '@/hooks/api';

import PostItem from './PostItem';

const Posts = () => {
  const { data } = useGetNewFeed();

  const posts = useMemo(
    () => data?.pages?.flatMap((page) => page.result)?.[0],
    [data],
  );

  console.log(posts);

  return (
    <div className="flex flex-col gap-3">
      {posts?.data.map((post) => (
        <PostItem key={post.id} post={post} />
      ))}
    </div>
  );
};

export default Posts;
