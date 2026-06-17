# PlateMate Design System

Extracted from Claude Design handoff bundle (June 2026). This file is the
permanent reference — the original design link is ephemeral.

## Design Language

Clean, light, modern mobile UI. Teal primary accent. System sans-serif font.
8dp grid. Subtle shadows. Pill-shaped chips. Turkish-plate-inspired badge with
left teal stripe.

---

## Color Palette

### Light Theme

| Token | Hex | Usage |
|---|---|---|
| `primary` | `#06B6D4` | CTA buttons, active tab, selected chips, links, accents |
| `primaryDark` | `#0E7490` | Plate badge text, primary text-on-container |
| `primaryContainer` | `#ECFEFF` | Plate badge bg, tag chip bg, avatar bg |
| `primaryContainerBorder` | `#CFF4FA` | Plate badge border, avatar border |
| `background` | `#F6F8FB` | Screen background (Ara, Kesfet, Profil) |
| `surface` | `#FFFFFF` | Cards, message list bg, bottom sheet bg |
| `surfaceSecondary` | `#F6F8FB` | Alternate surface (same as bg in light) |
| `cardBorder` | `#E9EDF2` | Card outline, divider between stat cells |
| `searchFieldBg` | `#EAEFF4` | Search bar background |
| `chipBg` | `#F1F5F9` | Inactive chip, recent search chip, divider |
| `textPrimary` | `#0F172A` | Titles, main text |
| `textSecondary` | `#475569` | Body text, inactive chip text |
| `textTertiary` | `#64748B` | Subtitles, captions, secondary info |
| `textLabel` | `#94A3B8` | Section labels (uppercase), timestamps |
| `outline` | `#E2E8F0` | Borders, chip borders, social icon borders |
| `star` | `#F59E0B` | Filled rating stars, warning status dot |
| `starEmpty` | `#E2E8F0` | Empty/unfilled stars |
| `success` | `#10B981` | Verified/approved status dot |
| `warning` | `#F59E0B` | In-review status dot (same as star) |
| `error` | `#EF4444` | Rejected status dot, error icon |
| `errorContainer` | `#FEF2F2` | Error state circle bg |
| `disabled` | `#CBD5E1` | Disabled button bg, clear icon bg |
| `skeleton` | `#E9EEF3` | Shimmer skeleton element bg |
| `skeletonSecondary` | `#EFF3F7` | Lighter skeleton variant |
| `tabInactive` | `#94A3B8` | Inactive tab icon + label |
| `tabActive` | `#06B6D4` | Active tab icon + label (= primary) |
| `onPrimary` | `#FFFFFF` | Text/icons on primary-colored surfaces |
| `ctaShadow` | `rgba(6,182,212,0.55)` | Teal glow on CTA buttons |
| `cardShadow` | `rgba(15,23,42,0.04)` | Subtle card elevation |

#### Category Colors

| Category | Background | Foreground | Icon |
|---|---|---|---|
| Teal | `#ECFEFF` | `#0E7490` | `#06B6D4` |
| Indigo | `#EEF2FF` | `#4338CA` | `#6366F1` |
| Orange | `#FFF7ED` | `#9A3412` | `#F59E0B` |
| Green | `#F0FDF4` | `#15803D` | `#22C55E` |

#### Avatar Colors (used for message/review avatars)

| Variant | Background | Foreground |
|---|---|---|
| Indigo | `#EEF2FF` | `#4F46E5` |
| Teal | `#ECFEFF` | `#0891B2` |
| Amber | `#FEF3C7` | `#B45309` |
| Rose | `#FFE4E6` | `#BE123C` |
| Violet | `#F3E8FF` | `#7C3AED` |
| Emerald | `#D1FAE5` | `#047857` |

#### Rank Badge Colors

| Rank | Background | Foreground |
|---|---|---|
| 1st | `#06B6D4` | `#FFFFFF` |
| Other | `#E2E8F0` | `#475569` |

### Dark Theme

| Token | Light | Dark |
|---|---|---|
| `primary` | `#06B6D4` | `#22D3EE` |
| `primaryDark` | `#0E7490` | `#67E8F9` |
| `primaryContainer` | `#ECFEFF` | `#164E63` |
| `primaryContainerBorder` | `#CFF4FA` | `#155E75` |
| `background` | `#F6F8FB` | `#0F172A` |
| `surface` | `#FFFFFF` | `#1E293B` |
| `surfaceSecondary` | `#F6F8FB` | `#162032` |
| `cardBorder` | `#E9EDF2` | `#334155` |
| `searchFieldBg` | `#EAEFF4` | `#1E293B` |
| `chipBg` | `#F1F5F9` | `#334155` |
| `textPrimary` | `#0F172A` | `#F1F5F9` |
| `textSecondary` | `#475569` | `#CBD5E1` |
| `textTertiary` | `#64748B` | `#94A3B8` |
| `textLabel` | `#94A3B8` | `#64748B` |
| `outline` | `#E2E8F0` | `#334155` |
| `star` | `#F59E0B` | `#FBBF24` |
| `starEmpty` | `#E2E8F0` | `#475569` |
| `success` | `#10B981` | `#34D399` |
| `warning` | `#F59E0B` | `#FBBF24` |
| `error` | `#EF4444` | `#F87171` |
| `errorContainer` | `#FEF2F2` | `#450A0A` |
| `disabled` | `#CBD5E1` | `#475569` |
| `skeleton` | `#E9EEF3` | `#334155` |
| `skeletonSecondary` | `#EFF3F7` | `#293548` |
| `tabInactive` | `#94A3B8` | `#64748B` |
| `tabActive` | `#06B6D4` | `#22D3EE` |
| `onPrimary` | `#FFFFFF` | `#FFFFFF` |
| `ctaShadow` | `rgba(6,182,212,0.55)` | `rgba(34,211,238,0.35)` |
| `cardShadow` | `rgba(15,23,42,0.04)` | `rgba(0,0,0,0.2)` |

---

## Typography

Font family: System default (sans-serif on Android, maps to Roboto/Google Sans).

| Style Name | Size (sp) | Weight | Letter Spacing | Line Height | Usage |
|---|---|---|---|---|---|
| `screenTitle` | 32 | 700 (Bold) | -0.6 | 1.2× | Screen titles (Plakalar, Kesfet, Mesajlar) |
| `sectionTitle` | 16 | 700 | 0 | 1.4× | "Şu An Trend", section headers |
| `cardTitle` | 15 | 700 | 0.3 | 1.3× | Plate number in list cards |
| `profileName` | 19 | 700 | 0 | 1.3× | Profile display name |
| `bigScore` | 30 | 800 | 0 | 1.0× | Large rating number (4.8) |
| `statValue` | 22 | 700 | 0 | 1.2× | Stats grid value (42, 318) |
| `plateCode` | 14–24 | 800 | -0.5 | 1.0× | City code in plate badge (varies by badge size) |
| `navTitle` | 16 | 600 | 0 | 1.4× | Navigation bar title |
| `buttonText` | 15–16 | 600–700 | 0 | 1.4× | CTA button labels |
| `bodyMedium` | 14 | 500 | 0 | 1.5× | Body text, descriptions |
| `bodySmall` | 13.5 | 500 | 0 | 1.5× | Review text, message preview |
| `caption` | 12–12.5 | 400–500 | 0 | 1.4× | City name, timestamps, subtitles |
| `chipText` | 13–13.5 | 600 | 0 | 1.0× | Chip labels |
| `sectionLabel` | 11 | 700 | 0.6 | 1.0× | Section labels, UPPERCASE, color=textLabel |
| `tabLabel` | 10.5 | 500 (inactive) / 600 (active) | 0.1 | 1.0× | Bottom nav tab labels |
| `statLabel` | 11 | 400 | 0 | 1.4× | Stats grid label |
| `starText` | 13 | 400 | 1.5 | 1.0× | Star characters (★★★★★) |

---

## Spacing & Sizing (8dp grid)

| Token | Value | Usage |
|---|---|---|
| `screenPaddingH` | 16dp | Horizontal content padding on all screens |
| `sectionGap` | 14dp | Vertical gap between screen sections |
| `cardPadding` | 11–14dp | Internal card padding |
| `cardGap` | 10dp | Gap between cards in lists/grids |
| `chipGap` | 8dp | Gap between chips |
| `itemGap` | 12dp | Gap between items in message/review lists |
| `iconGapSmall` | 4dp | Gap between icon and label in tab bar |
| `avatarSizeSmall` | 36dp | Review avatars |
| `avatarSizeMedium` | 46dp | Message avatars |
| `avatarSizeLarge` | 80dp | Profile avatar |
| `plateBadgeSmall` | 38×38dp | Saved plates badge |
| `plateBadgeMedium` | 44×44dp | Trending list badge |
| `plateBadgeLarge` | 64×64dp | Detail screen badge |
| `plateBadgeReview` | 52×52dp | Review screen badge |
| `searchBarHeight` | 44dp | Search input height |
| `ctaHeight` | 50–52dp | Primary CTA button height |
| `chipHeight` | 30–36dp | Chip/tag height |
| `categoryCardHeight` | 104dp | Discover category grid cards |
| `tabBarHeight` | 83dp | Bottom navigation bar |
| `socialIconSize` | 44dp | Profile social media circle buttons |
| `rankBadgeSize` | 28dp | Trending rank number badge |
| `statusDotSize` | 10dp | Status indicator dots |
| `unreadDotSize` | 9dp | Message unread indicator |
| `savedPlateCardWidth` | 150dp | Horizontal scroll saved plate card |

---

## Corner Radii

| Token | Value | Usage |
|---|---|---|
| `radiusCard` | 14dp | Standard cards, trend items, search bar |
| `radiusCardLarge` | 16dp | Category cards, status card, activity card |
| `radiusChip` | 999dp | Chips, pills, tags (full round) |
| `radiusCta` | 14dp | CTA buttons |
| `radiusSearchBar` | 13dp | Search input field |
| `radiusPlateBadgeSmall` | 9dp | Small plate badges |
| `radiusPlateBadgeMedium` | 11dp | Medium plate badges |
| `radiusPlateBadgeLarge` | 15dp | Large plate badges |
| `radiusAvatar` | 50% | Circular avatars |
| `radiusRankBadge` | 9dp | Rank number badge |
| `radiusSocialIcon` | 50% | Social media circle buttons |
| `radiusCategoryIcon` | 10dp | Category card icon container |
| `radiusCheckbox` | 6dp | Anonymous toggle checkbox |

---

## Elevation / Shadows

| Token | Value | Usage |
|---|---|---|
| `elevationCard` | `0 2px 8px rgba(15,23,42,0.04)` | Standard cards |
| `elevationCta` | `0 8px 20px -6px rgba(6,182,212,0.55)` | Teal CTA buttons |
| `elevationCtaSmall` | `0 6px 16px -4px rgba(6,182,212,0.5)` | Small teal buttons (Ara) |
| `elevationCategoryIcon` | `0 1px 3px rgba(15,23,42,0.08)` | Category icon bg |
| `elevationNone` | none | Flat elements |

---

## Component Specs

### PMPlateBadge

Rounded rectangle with left teal stripe. Three sizes:

| Size | Dimensions | Radius | Stripe Width | Code Font Size | Padding Left |
|---|---|---|---|---|---|
| Small | 38×38dp | 9dp | 6dp | 14sp/800wt | 5dp |
| Medium | 44×44dp | 11dp | 6dp | 16sp/800wt | 5dp |
| Large | 64×64dp | 15dp | 9dp | 24sp/800wt | 8dp |
| Review | 52×52dp | 13dp | 8dp | 19sp/800wt | 7dp |

- Background: `primaryContainer` (#ECFEFF)
- Border: 1dp `primaryContainerBorder` (#CFF4FA)
- Stripe: `primary` (#06B6D4)
- Text color: `primaryDark` (#0E7490)

### PMSearchBar

- Height: 44dp
- Background: `searchFieldBg` (#EAEFF4)
- Radius: 13dp
- Search icon: 18dp, stroke `textTertiary` (#64748B)
- Placeholder text: 15sp, `textLabel` (#94A3B8)
- Input text: 15sp/500wt, `textPrimary`
- "Ara" button: adjacent, primary bg, 13dp radius, 20dp horizontal padding, 15sp/700wt white text
- Disabled Ara button: `disabled` bg (#CBD5E1)
- Loading state: spinner icon replaces Ara button, inside search bar at right

### PMButton (CTA)

- Height: 50–52dp
- Background: `primary` (#06B6D4)
- Radius: 14dp
- Shadow: `elevationCta`
- Text: 16sp/600wt, white
- Icon: optional leading, 18–19dp, white stroke
- Disabled: bg `disabled` (#CBD5E1), no shadow

### PMCard

- Background: `surface` (#FFFFFF)
- Border: 1dp `cardBorder` (#E9EDF2)
- Radius: 14dp (standard) or 16dp (large variant)
- Shadow: `elevationCard`
- Padding: 11–14dp

### PMTrendCard

- PMCard wrapper (14dp radius, white, border, shadow)
- Padding: 11dp
- Layout: Row — rank badge (28dp circle/rounded), plate badge (medium), column (plate number 15sp/700wt + caption with star)
- Rank badge: rounded 9dp, rank 1 = primary bg + white text, others = #E2E8F0 bg + #475569 text

### PMCategoryCard

- Background: category color
- Radius: 16dp
- Height: 104dp
- Padding: 14dp
- Layout: Column — icon container at top, title + count at bottom
- Icon container: 36dp, radius 10dp, white bg, shadow `elevationCategoryIcon`, inner colored dot 14dp/5dp radius
- Title: 14sp/700wt, category foreground color
- Count: 12sp, `textTertiary`

### PMMessageItem

- Layout: Row — avatar (46dp circle) + column (name 15sp/600wt + preview 13sp/textTertiary) + column (time 12sp/textLabel + unread dot)
- Bottom border: 1dp `chipBg` (#F1F5F9)
- Unread dot: 9dp circle, `primary`
- Padding: 13dp vertical, 16dp horizontal

### PMRatingStars

- Star size: 34dp (interactive input) / 11–12dp (display inline)
- Filled: `star` (#F59E0B)
- Empty: `starEmpty` (#E2E8F0)
- Gap: 9dp (input) / 0dp (inline text)

### PMRatingBar

- Row: number (11sp/600wt, textTertiary) + star icon (11dp) + bar (flex, 7dp height, 4dp radius, bg=#EEF2F6, fill=primary) + percent (11sp, textLabel)
- Gap: 9dp between elements

### PMTagChip

- Height: 34dp (review screen) / 30dp (detail screen)
- Radius: 999dp (pill)
- Padding: 0 15dp horizontal
- Selected: bg `primary`, text white, border `primary`
- Unselected: bg white, text `textSecondary`, border `outline`
- Font: 13.5sp/600wt

### PMStatCard

- Background: `surface`
- Border: 1dp `cardBorder`
- Radius: 14dp
- Padding: 13dp vertical, 8dp horizontal
- Layout: Column center — value (22sp/700wt) + label (11sp, textTertiary)

### Section Label

- Font: 11sp, weight 700, letter-spacing 0.6sp
- Color: `textLabel` (#94A3B8)
- Transform: UPPERCASE
- Can be a `PMText` variant or standalone composable

### Bottom Nav (TabBar)

- Height: 83dp (includes home indicator area)
- Background: surface with 94% opacity
- Top border: 1dp `cardBorder`
- 4 tabs: Ara (search), Kesfet (compass), Mesajlar (chat), Profil (person)
- Icon: 25dp, stroke style
- Label: 10.5sp, gap 4dp below icon
- Active: color `primary`, weight 600
- Inactive: color `tabInactive` (#94A3B8), weight 500
- Tab width: 66dp each

---

## Screen Layouts

### 01 · Ara (Search — Tab 1)
- Background: `background`
- Title: "Plakalar" (screenTitle style)
- Search bar + Ara button row
- Section "SON ARAMALAR": wrap-row of recent search chips (chipBg, pill, 13sp/600wt)
- Section "KAYDEDİLEN PLAKALAR": header row (label + "Tümü" link in primary), horizontal scroll of saved plate cards (150dp wide, plate badge + bookmark icon + plate number + rating/city)
- State variants: Empty (illustration + CTA text), Loading (spinner + skeleton), No Results (search icon circle + message + clear link), Error (red ! circle + message + retry button)

### 02 · Kesfet (Discover — Tab 2)
- Background: `background`
- Title: "Keşfet" + subtitle "Trend & popüler plakalar"
- Horizontal filter chips (Tümü active, En İyi, En Çok Yorum, Yeni)
- Section "🔥 Şu An Trend": trending cards list
- Section "KATEGORİLER": 2-column grid of category cards

### 03 · Mesajlar (Messages — Tab 3)
- Background: `surface` (white)
- Title: "Mesajlar" + "+" icon (primary stroke)
- Message list: PMMessageItem rows, border dividers

### 04 · Profil (Profile — Tab 4)
- Background: `background`
- Settings gear icon top-right
- Avatar (80dp, primaryContainer bg, initials)
- Name (19sp/700wt) + subtitle (12.5sp, textLabel)
- Stats 3-grid (PMStatCard)
- Section "PLAKA DURUMU": 3-column in card (status dot + value + label)
- Section "SOSYAL MEDYA": row of 44dp circle icon buttons
- Section "SON AKTİVİTE": activity card (rows: colored icon box + text + time)

### Plaka Detayı (Plate Detail)
- Background: `surface` (white)
- Top bar: back chevron + "Plaka Detayı" + bookmark icon
- Plate info row: large badge + plate number (20sp/800wt) + city + score (30sp/800wt) + stars + review count
- Rating breakdown: 5→1 bars (PMRatingBar)
- Section "ETİKETLER": tag chips with counts
- Section "DEĞERLENDİRMELER": review items (avatar + name + stars + time + text)
- Fixed bottom: "Değerlendir" CTA button

### Değerlendir (Review)
- Background: `surface` (white)
- Top bar: back chevron + "Değerlendir" + spacer
- Plate info card (surfaceSecondary bg, border, 16dp radius): review-size badge + plate + city/count
- Section "GENEL PUAN": 5 interactive stars (34dp) + verbal label
- Section "DETAYLI PUANLAMA": card with sub-rating rows (label + filled/empty stars)
- Section "ETİKETLER": 12 selectable tag chips (3 pre-selected teal, 9 unselected)
- Section "DENEYİMİN": text area (14dp radius, border, placeholder, char counter 0/240)
- Fixed footer: anonymous checkbox row + "Değerlendirmeyi Paylaş" CTA
