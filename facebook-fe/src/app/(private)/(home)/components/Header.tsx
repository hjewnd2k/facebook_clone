import { Avatar, AvatarFallback, AvatarImage } from '@radix-ui/react-avatar';
import {
  Bell,
  Gamepad2,
  House,
  List,
  MessageCircle,
  MonitorPlay,
  Store,
  UsersRound,
} from 'lucide-react';
import Link from 'next/link';

import { FacebookIcon } from '@/assets/svgs';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Input } from '@/components/ui/input';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { cn } from '@/lib/utils';

const Header = () => {
  const tabs = [
    { url: '/', label: 'Home', icon: <House /> },
    { url: '/watch', label: 'Watch', icon: <MonitorPlay /> },
    { url: '/marketplace', label: 'Marketplace', icon: <Store /> },
    { url: '/groups', label: 'Groups', icon: <UsersRound /> },
    { url: '/gaming', label: 'Gaming', icon: <Gamepad2 /> },
  ];

  return (
    <header
      className={cn(
        'fixed top-0 left-0 flex h-(--header-height) w-full items-center justify-between bg-white shadow',
        'px-4',
      )}
    >
      <div className="flex flex-1 items-center gap-2">
        <Link
          href={'/'}
          className="flex size-10 items-center justify-center rounded-full"
        >
          <FacebookIcon className="fill-blue-500" />
        </Link>
        <Input placeholder="Tìm kiếm trên Facebook" className="max-w-50" />
      </div>
      <div defaultValue="home">
        {tabs.map((tab) => (
          <Button key={tab.url} asChild variant={'ghost'} size={'icon-lg'}>
            <Link href={tab.url}>{tab.icon}</Link>
          </Button>
        ))}
      </div>
      <div className="flex flex-1 items-center justify-end gap-2">
        <Button variant={'secondary'} className="size-10 rounded-full">
          <List />
        </Button>
        <Button variant={'secondary'} className="size-10 rounded-full">
          <MessageCircle />
        </Button>
        <Button variant={'secondary'} className="size-10 rounded-full">
          <Bell />
        </Button>

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant={'secondary'} size={'icon'}>
              <Avatar>
                <AvatarImage
                  className="rounded-full"
                  src="https://github.com/shadcn.png"
                />
                <AvatarFallback>CN</AvatarFallback>
              </Avatar>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>Profile</DropdownMenuItem>
            <DropdownMenuItem>Billing</DropdownMenuItem>
            <DropdownMenuItem>Team</DropdownMenuItem>
            <DropdownMenuItem>Subscription</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  );
};

export default Header;
