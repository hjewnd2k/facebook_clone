import { Slot } from '@radix-ui/react-slot';
import { type VariantProps, cva } from 'class-variance-authority';
import * as React from 'react';

import { cn } from '@/lib/utils';

type HtmlTags = Exclude<
  keyof React.JSX.IntrinsicElements,
  keyof SVGElementTagNameMap
>;

const variantToTag: Record<string, HtmlTags> = {
  h1: 'h1',
  h2: 'h2',
  h3: 'h3',
  h4: 'h4',
  body: 'p',
  secondary: 'p',
  muted: 'p',
  subheading: 'p',
  subtitle: 'p',
  caption: 'span',
  large: 'p',
};

const typographyVariants = cva('', {
  variants: {
    variant: {
      h1: 'text-2xl font-bold',
      h2: 'text-xl font-semibold',
      h3: 'text-lg font-semibold',
      h4: 'text-base font-medium',
      body: 'text-base',
      title: 'text-base font-semibold',
      subtitle: 'text-sm',
      secondary: 'text-sm font-medium',
      muted: 'text-sm text-muted-foreground',
      caption: 'text-xs text-muted-foreground',
      large: 'text-lg font-bold leading-7',
    },
  },
  defaultVariants: {
    variant: 'body',
  },
});

function Typography({
  className,
  variant,
  asChild = false,
  ...props
}: React.HTMLAttributes<HTMLElement> &
  VariantProps<typeof typographyVariants> & {
    asChild?: boolean;
  }) {
  const Tag = asChild ? Slot : variant ? variantToTag[variant] || 'p' : 'p';

  return (
    <Tag
      data-slot="typography"
      className={cn(typographyVariants({ variant, className }))}
      {...props}
    />
  );
}

export { Typography, typographyVariants };
