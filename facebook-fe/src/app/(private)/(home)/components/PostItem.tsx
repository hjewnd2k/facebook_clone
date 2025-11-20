import { Ellipsis, MessageCircle, Share2, ThumbsUp, X } from 'lucide-react';
import Image from 'next/image';
import { useMemo } from 'react';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from '@/components/ui/tooltip';
import { Typography } from '@/components/ui/typography';
import { useInteractionReact } from '@/hooks/api';
import { cn } from '@/lib/utils';
import { NewFeed } from '@/types';
import { ReactionType } from '@/types/enums';

type Props = {
  post: NewFeed;
};

const PostItem = ({ post }: Props) => {
  const images = useMemo(() => post.media || [], [post]);
  const imageCount = useMemo(() => images.length, [images]);

  const likeCount = post.likeCount || 0;
  const commentCount = post.commentCount || 0;

  const likers = post.likers || [];

  const { mutateAsync } = useInteractionReact(post.id);

  const renderLikeSummary = () => {
    if (likeCount === 0) return null;

    const othersCount = Math.max(0, likeCount - likers.length);

    return (
      <div className="flex items-center gap-2 text-sm">
        {/* Icon like */}
        <div className="flex -space-x-1">
          <div className="flex h-5 w-5 items-center justify-center rounded-full bg-blue-500">
            <ThumbsUp className="h-3 w-3 text-white" />
          </div>
        </div>

        {/* Tên người thích */}
        <span className="text-gray-700">
          {likers.slice(0, 3).map((liker, i) => (
            <span key={i}>
              {i > 0 && i === likers.length - 1 ? ' và ' : i > 0 ? ', ' : ''}
              <span className="cursor-pointer font-medium hover:underline">
                {liker.displayName}
              </span>
            </span>
          ))}
          {othersCount > 0 && (
            <span className="text-gray-600">
              {' '}
              và{' '}
              <span className="cursor-pointer font-medium hover:underline">
                {othersCount} người khác
              </span>
            </span>
          )}
        </span>
      </div>
    );
  };

  const renderImages = () => {
    if (imageCount === 0) return null;

    if (imageCount === 1) {
      return (
        <div className="relative aspect-video overflow-hidden bg-gray-100">
          <Image
            src={images[0].url}
            alt=""
            fill
            className="object-cover"
            sizes="(max-width: 768px) 100vw, 600px"
          />
        </div>
      );
    }

    if (imageCount === 2) {
      return (
        <div className="grid grid-cols-2 gap-1">
          {images.slice(0, 2).map((img, i) => (
            <div
              key={i}
              className="relative aspect-square overflow-hidden bg-gray-100"
            >
              <Image src={img.url} alt="" fill className="object-cover" />
            </div>
          ))}
        </div>
      );
    }

    if (imageCount === 3) {
      return (
        <div className="grid grid-cols-2 gap-1">
          <div className="relative aspect-square overflow-hidden bg-gray-100">
            <Image src={images[0].url} alt="" fill className="object-cover" />
          </div>
          <div className="grid grid-rows-2 gap-1">
            {images.slice(1).map((img, i) => (
              <div
                key={i}
                className="relative aspect-square overflow-hidden bg-gray-100"
              >
                <Image src={img.url} alt="" fill className="object-cover" />
              </div>
            ))}
          </div>
        </div>
      );
    }

    if (imageCount === 4) {
      return (
        <div className="grid grid-cols-2 gap-1">
          {images.slice(0, 4).map((img, i) => (
            <div
              key={i}
              className="relative aspect-square overflow-hidden bg-gray-100"
            >
              <Image src={img.url} alt="" fill className="object-cover" />
            </div>
          ))}
        </div>
      );
    }

    return (
      <div className="grid grid-cols-2 gap-1">
        {images.slice(0, 4).map((img, i) => (
          <div
            key={i}
            className={cn(
              'relative aspect-square overflow-hidden bg-gray-100',
              i === 3 && imageCount > 4 && 'group cursor-pointer',
            )}
          >
            <Image src={img.url} alt="" fill className="object-cover" />
            {i === 3 && imageCount > 4 && (
              <div className="absolute inset-0 flex items-center justify-center bg-black/50 text-3xl font-bold text-white transition group-hover:bg-black/60">
                +{imageCount - 4}
              </div>
            )}
          </div>
        ))}
      </div>
    );
  };

  const handleLike = async (reactionType: ReactionType) => {
    try {
      await mutateAsync({ reactionType });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="overflow-hidden rounded-lg border border-gray-200 bg-white shadow-sm">
      {/* Header */}
      <div className="flex justify-between p-3">
        <div className="flex items-center gap-3">
          <Avatar className="h-10 w-10">
            <AvatarImage src={post.userResponse.profilePictureUrl} />
            <AvatarFallback>
              {post.userResponse.displayName.charAt(0).toUpperCase()}
            </AvatarFallback>
          </Avatar>
          <div>
            <Typography
              variant="title"
              className="cursor-pointer text-base font-semibold hover:underline"
            >
              {post.userResponse.displayName}
            </Typography>
            <Typography variant="subtitle" className="text-xs text-gray-500">
              6 giờ trước
            </Typography>
          </div>
        </div>
        <div className="flex gap-1">
          <Button variant="ghost" size="icon" className="h-9 w-9 rounded-full">
            <Ellipsis className="h-5 w-5 text-gray-600" />
          </Button>
          <Button variant="ghost" size="icon" className="h-9 w-9 rounded-full">
            <X className="h-5 w-5 text-gray-600" />
          </Button>
        </div>
      </div>

      {/* Nội dung */}
      {post.content && (
        <div className="px-3 pb-2 text-base">{post.content}</div>
      )}

      {/* Ảnh */}
      {imageCount > 0 && <div className="mt-1">{renderImages()}</div>}

      {/* Like + Comment Summary */}
      {(likeCount > 0 || commentCount > 0) && (
        <div className="border-t border-gray-100 px-3 py-2">
          <div className="flex items-center justify-between">
            {/* Like summary */}
            <div className="flex-1">{renderLikeSummary()}</div>

            {/* Comment count */}
            {commentCount > 0 && (
              <button className="text-sm text-gray-600 hover:underline">
                {commentCount} bình luận
              </button>
            )}
          </div>
        </div>
      )}

      {/* Action buttons */}
      <div className="border-t border-gray-200 px-3 py-2">
        <div className="flex justify-around">
          <Tooltip delayDuration={300}>
            <TooltipTrigger asChild>
              <Button
                variant="ghost"
                className="flex flex-1 items-center justify-center gap-2 rounded-lg py-2 hover:bg-gray-100"
                onClick={() => handleLike(ReactionType.LIKE)}
              >
                <ThumbsUp className="h-5 w-5 text-gray-600" />
                <span className="font-medium text-gray-700">Thích</span>
              </Button>
            </TooltipTrigger>
            <TooltipContent className="[&_svg]:bg-card [&_svg]:fill-card bg-transparent p-0">
              <Card className="p-0">
                <CardContent className="flex items-center gap-2 px-2 py-1">
                  <picture className="cursor-pointer">
                    <source
                      srcSet="https://fonts.gstatic.com/s/e/notoemoji/latest/1f44d/512.webp"
                      type="image/webp"
                    />
                    <img
                      src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f44d/512.gif"
                      alt="👍"
                      width="32"
                      height="32"
                    />
                  </picture>
                  <picture className="cursor-pointer">
                    <source
                      srcSet="https://fonts.gstatic.com/s/e/notoemoji/latest/2764_fe0f/512.webp"
                      type="image/webp"
                    />
                    <img
                      src="https://fonts.gstatic.com/s/e/notoemoji/latest/2764_fe0f/512.gif"
                      alt="❤"
                      width="32"
                      height="32"
                    />
                  </picture>

                  <picture className="cursor-pointer">
                    <source
                      srcSet="https://fonts.gstatic.com/s/e/notoemoji/latest/1f600/512.webp"
                      type="image/webp"
                    />
                    <img
                      src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f600/512.gif"
                      alt="😀"
                      width="32"
                      height="32"
                    />
                  </picture>
                </CardContent>
              </Card>
            </TooltipContent>
          </Tooltip>

          <Button
            variant="ghost"
            className="flex flex-1 items-center justify-center gap-2 rounded-lg py-2 hover:bg-gray-100"
          >
            <MessageCircle className="h-5 w-5 text-gray-600" />
            <span className="font-medium text-gray-700">Bình luận</span>
          </Button>
          <Button
            variant="ghost"
            className="flex flex-1 items-center justify-center gap-2 rounded-lg py-2 hover:bg-gray-100"
          >
            <Share2 className="h-5 w-5 text-gray-600" />
            <span className="font-medium text-gray-700">Chia sẻ</span>
          </Button>
        </div>
      </div>
    </div>
  );
};

export default PostItem;
