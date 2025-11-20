import Image from 'next/image';
import React from 'react';

import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';

const Stories = () => {
  return (
    <Carousel
      opts={{
        align: 'start',
      }}
      className="my-3 w-full"
    >
      <CarouselContent>
        {Array.from({ length: 20 }).map((_, index) => (
          <CarouselItem key={index} className="ml-4 h-55 basis-28 pl-0">
            <Card className="relative h-full w-full overflow-hidden">
              <Image
                src="https://anhnghethuatvietnam2022.com/wp-content/uploads/2025/03/Hinh-gai-xinh-Viet-Nam-cute-2.webp"
                alt="Shadcn"
                className="h-full w-full object-cover"
                fill
                sizes="(max-width: 768px) 100vw, 33vw"
              />
            </Card>
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious />
      <CarouselNext />
    </Carousel>
  );
};

export default Stories;
