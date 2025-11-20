'use client';

import { Images, Laugh, Video } from 'lucide-react';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { useUserStore } from '@/stores/user.store';

const NewPost = () => {
  const { user } = useUserStore();

  return (
    <div className="flex items-center gap-2 rounded-2xl bg-white p-4 shadow">
      <Avatar>
        <AvatarImage src="https://github.com/shadcn.png" />
        <AvatarFallback>{user?.displayName}</AvatarFallback>
      </Avatar>
      <Input placeholder={`${user?.displayName} ơi, Bạn đang nghĩ gì thế?`} />
      <Button variant={'ghost'} size={'icon-lg'}>
        <Video className="size-6 text-red-500" />
      </Button>
      <Button variant={'ghost'} size={'icon-lg'}>
        <Images className="size-6 text-green-500" />
      </Button>
      <Button variant={'ghost'} size={'icon-lg'}>
        <Laugh className="size-6 text-yellow-500" />
      </Button>
    </div>
  );
};

export default NewPost;
