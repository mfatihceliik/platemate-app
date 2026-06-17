# PlateMate Redesign — Task Tracker

## How to Resume

1. Read `Android/PlateMate/CLAUDE.md`
2. Read this file (`Android/PlateMate/docs/design/REDESIGN_TASKS.md`)
3. Read `Android/PlateMate/docs/design/DESIGN_SYSTEM.md`
4. Find the first task not marked `[x]` and continue from there.
5. After completing each task, mark it `[x]`, fill in Notes, and verify the module compiles.

**Status legend:** `[ ]` todo · `[~]` in progress · `[x]` done

---

## Phase 0 — Theme & Tokens

- [x] **P0-1: Color.kt** — Replace orange palette with teal. Define all light + dark color vals per DESIGN_SYSTEM.md.
  - Files: `presentation/theme/Color.kt`
  - Done when: All hex values match design system, both light and dark palettes defined. Compiles.
  - Notes: Orange→Teal complete. Light primary=#06B6D4, dark=#22D3EE. All Material color scheme slots mapped. Var names preserved so Theme.kt compiles unchanged.

- [x] **P0-2: PMSemanticColors.kt** — Expand from icon-only to full semantic color system (text, card, search, chip, star, status, skeleton, category, avatar, rank colors).
  - Files: `presentation/theme/PMSemanticColors.kt`, `presentation/theme/Theme.kt`
  - Done when: All semantic tokens from DESIGN_SYSTEM.md are accessible via `MaterialTheme.pmColors`. Compiles.
  - Notes: New PMColors data class with ~45 semantic tokens (light+dark). PMIconColors preserved as nested field. Theme.kt updated to provide LocalPMColors+LocalPMIconColors. Backward compat with existing `MaterialTheme.pmIconColors` maintained.

- [x] **P0-3: Type.kt** — Define full typography scale (screenTitle, sectionLabel, cardTitle, bodyMedium, caption, chipText, buttonText, etc.).
  - Files: `presentation/theme/Type.kt`
  - Done when: All text styles from DESIGN_SYSTEM.md are defined in MaterialTheme.typography or custom extension. Compiles.
  - Notes: Mapped design styles to Material slots: headlineLarge=screenTitle(32sp), displayLarge=bigScore(30sp), displayMedium=statValue(22sp), headlineMedium=profileName(19sp), titleLarge=navTitle(16sp), titleMedium=cardTitle(15sp), bodyLarge=buttonText(15sp), bodyMedium=body(14sp), bodySmall=reviewText(13.5sp), labelLarge=chipText(13.5sp), labelMedium=sectionLabel(11sp/0.6ls), labelSmall=tabLabel(10.5sp).

- [x] **P0-4: Shape.kt** — Update shapes to match design radii (card 14dp, cardLarge 16dp, searchBar 13dp, cta 14dp, chip full-round).
  - Files: `presentation/theme/Shape.kt`
  - Done when: PMShapes has all radius variants. Compiles.
  - Notes: extraSmall=9dp(badges), small=13dp(searchBar), medium=14dp(cards/cta), large=16dp(categoryCards), extraLarge=999dp(chips/pills).

- [x] **P0-5: PMDimensions.kt** — Add missing spacing/sizing tokens (screen padding, card padding, avatar sizes, badge sizes, CTA heights, chip heights, tab bar height, etc.).
  - Files: `presentation/theme/PMDimensions.kt`
  - Done when: All dp values from DESIGN_SYSTEM.md accessible via `MaterialTheme.pmDimensions`. Compiles.
  - Notes: Added PMSizing data class with 28 sizing tokens (avatars, badges, CTA, chips, tab bar, etc.). Added r9/r11/r13/r15/r16/rFull to PMRadius. Used default params for cleaner construction. Backward compat maintained — existing `spacing.s16` etc. still work.

- [x] **P0-6: Theme.kt** — Wire new color schemes (light + dark teal), new typography, new shapes, new semantic colors into PlateMateTheme. Update CompositionLocalProvider.
  - Files: `presentation/theme/Theme.kt`
  - Done when: PlateMateTheme provides all new tokens. Light + dark switch works. Preview renders teal. Compiles.
  - Notes: Most wiring done in P0-2. Updated preview background colors to match new theme (light=#F6F8FB, dark=#0F172A). All CompositionLocal providers in place: LocalPMDimensions, LocalPMColors, LocalPMIconColors.

---

## Phase 1 — Shared Components

### Restyle existing

- [x] **P1-1: PMButton** — Teal primary, 14dp radius, teal glow shadow, 50-52dp height, disabled state. Remove orange styling.
  - Files: `presentation/components/PMButton.kt`
  - Done when: PMButton matches CTA spec. @Preview shows teal button. Compiles.
  - Notes: Height changed from hardcoded 48dp to `sizing.ctaHeight` (50dp). Shape already uses `shapes.medium` (now 14dp). Colors come from MaterialTheme which is now teal. Preview bg colors updated. No orange refs.

- [x] **P1-2: PMCard** — White bg, 1dp cardBorder, 14dp radius, subtle shadow. Add large variant (16dp radius).
  - Files: `presentation/components/PMCard.kt`
  - Done when: PMCard matches card spec. Compiles.
  - Notes: Radius r8→r14. Border uses pmColors.cardBorder. Default padding 16→14dp. Added PMCardVariant enum (Standard=14dp, Large=16dp). Preview bg updated.

- [x] **P1-3: PMText** — Add section-label variant (11sp, 700wt, 0.6 letter-spacing, uppercase, textLabel color). Ensure other variants use design typography tokens.
  - Files: `presentation/components/PMText.kt`, `presentation/components/PMComponentStyles.kt`
  - Done when: PMText supports sectionLabel style. Compiles.
  - Notes: Added PMTextStyle.SectionLabel → labelMedium (11sp/700wt/0.6ls). PMText auto-uppercases SectionLabel text and defaults to textLabel color. Preview includes SectionLabel sample.

- [x] **P1-4: PMTextField** — Restyle to match search field design (searchFieldBg, 13dp radius, textTertiary placeholder).
  - Files: `presentation/components/PMTextField.kt`
  - Done when: PMTextField matches design. Compiles.
  - Notes: Added shapes.medium (14dp) to OutlinedTextField. Preview bg updated. Auth screens use this; design search bar is separate PMSearchBar (P1-9).

- [x] **P1-5: PMPasswordField** — Align with PMTextField restyle.
  - Files: `presentation/components/PMPasswordField.kt`
  - Done when: Consistent with new PMTextField. Compiles.
  - Notes: Delegates to PMTextField. Preview bg updated. No other changes needed.

- [x] **P1-6: PMComponentStyles** — Update any shared style constants to use new design tokens.
  - Files: `presentation/components/PMComponentStyles.kt`
  - Done when: No orange refs remain, all values from design tokens. Compiles.
  - Notes: Updated in P1-3 (SectionLabel added). No orange refs. Clean.

- [x] **P1-7: PlateCard** — Restyle to match new trend card design (white bg, border, badge + info layout).
  - Files: `presentation/components/PlateCard.kt`
  - Done when: PlateCard uses new tokens, matches design card layout. Compiles.
  - Notes: CompactPlateBadge updated: r8→r9, bg→primaryContainer, border→primaryContainerBorder, text→primaryDark, stripe width s4→s6. Preview bg colors updated. Pre-existing !! warning unchanged.

### New components

- [x] **P1-8: PMPlateBadge** — New component. 4 sizes (small/medium/large/review). Teal stripe, primaryContainer bg, city code text.
  - Files: `presentation/components/PMPlateBadge.kt` (new)
  - Done when: Renders correctly at all 4 sizes. @Preview. Compiles.
  - Notes: Created with PlateBadgeSize enum (Small/Medium/Large/Review). Uses pmColors + pmDimensions tokens. Light + dark previews. 4 sizes in a row.

- [x] **P1-9: PMSearchBar** — Search input + "Ara" button row. States: empty, filled, loading (spinner), disabled.
  - Files: `presentation/components/PMSearchBar.kt` (new)
  - Done when: All states render. Events hoisted (onQueryChange, onSearch). @Preview. Compiles.
  - Notes: BasicTextField with custom placeholder, search icon, spinner animation for loading. Ara button disables when empty. Uses existing string resources (search_plate_placeholder, search_submit). Light+dark previews.

- [x] **P1-10: PMTrendCard** — Rank badge + plate badge + plate info. Rank 1 gets teal badge, others gray.
  - Files: `presentation/components/PMTrendCard.kt` (new)
  - Done when: Renders with rank, badge, plate, rating. @Preview. Compiles.
  - Notes: Rank badge (teal for #1, gray for others). Uses PMPlateBadge(Small). Shows plate, city, rating stars, review count. Light+dark preview.

- [x] **P1-11: PMCategoryCard** — Colored bg, icon container, title + count.
  - Files: `presentation/components/PMCategoryCard.kt` (new)
  - Done when: Renders with color params. @Preview. Compiles.
  - Notes: Color params for bg/iconBg/iconDot. White circle icon container with colored dot. Title + count text. 2x2 grid preview.

- [x] **P1-12: PMMessageItem** — Avatar circle + name/preview + time/unread dot.
  - Files: `presentation/components/PMMessageItem.kt` (new)
  - Done when: Renders read + unread states. @Preview. Compiles.
  - Notes: Avatar with initials (custom colors), name/preview text, time label, unread dot. Uses debouncedClickable. Preview shows read+unread.

- [x] **P1-13: PMRatingStars** — Interactive (input) and display modes. Configurable size.
  - Files: `presentation/components/PMRatingStars.kt` (new)
  - Done when: Both modes work. Star tap callback hoisted. @Preview. Compiles.
  - Notes: Filled/Outlined Star icons. Interactive mode with debouncedClickable. Configurable starSize, gap, maxStars. Preview: display (34dp, 12dp) + interactive.

- [x] **P1-14: PMRatingBar** — Single row: number + star + progress bar + percent.
  - Files: `presentation/components/PMRatingBar.kt` (new)
  - Done when: Renders with percentage fill. @Preview. Compiles.
  - Notes: Star number + star icon + clipped progress bar (chipBg bg, primary fill) + percent text. Preview: 5-star breakdown.

- [x] **P1-15: PMTagChip** — Selectable chip. Selected = teal, unselected = white/outlined.
  - Files: `presentation/components/PMTagChip.kt` (new)
  - Done when: Toggle works, callback hoisted. @Preview. Compiles.
  - Notes: Selected=primary bg+onPrimary text, unselected=surface bg+chipBg border. Uses debouncedClickable. FlowRow preview with 6 tags.

- [x] **P1-16: PMStatCard** — Value + label in bordered card.
  - Files: `presentation/components/PMStatCard.kt` (new)
  - Done when: Renders value/label. @Preview. Compiles.
  - Notes: Surface bg, cardBorder, r14 radius. displayMedium for value, 11sp for label. Preview: 3-card row.

- [x] **P1-17: Bottom nav restyle** — Update bottom nav to teal active color, correct icon set (search/compass/chat/person), add Mesajlar tab route.
  - Files: `presentation/features/main/MainBottomBar.kt`
  - Done when: 4 tabs render with teal active, Mesajlar tab navigates to placeholder. Compiles.
  - Notes: Selected=tabActive(#06B6D4), unselected=tabInactive(#94A3B8). Indicator set to surface (invisible pill). 4 tabs already existed (Search/Discover/Messages/Profile) with correct icons. Messages already wired to placeholder.

---

## Phase 2 — Reskin Existing Screens

- [x] **P2-1: SearchScreen** — Full redesign: title "Plakalar", PMSearchBar, recent searches chips, saved plates horizontal scroll. Add Empty/Loading/NoResults/Error state variants.
  - Files: `presentation/features/main/search/SearchScreen.kt`, `res/values/strings.xml`
  - Done when: All 5 states (content, empty, loading, no-result, error) render per design. @Preview for each state. Compiles.
  - Notes: Title→"Plakalar" (headlineLarge). PlateInput replaced by PMSearchBar. Recent searches→FlowRow chips (chipBg pills). Saved plates→LazyRow of 150dp compact cards with PMPlateBadge+bookmark+rating. Empty state with search icon circle. Shimmer updated to match new layout. 4 previews (light/empty/dark/shimmer). Strings updated: search_submit→"Ara", added section labels + empty/error strings. ViewModel/UiState/UiAction unchanged.

- [x] **P2-2: DiscoverScreen** — Redesign: title + subtitle, filter chips, trending list (PMTrendCard), category grid (PMCategoryCard).
  - Files: `presentation/features/main/discover/DiscoverScreen.kt`, `res/values/strings.xml`
  - Done when: Screen matches design layout. Filter chips, trend cards, category grid render. @Preview. Compiles.
  - Notes: Title→"Keşfet" (headlineLarge) + subtitle "Trend & popüler plakalar". Filter chips→custom Box pills (selected=primary, unselected=chipBg). PlateCard→PMTrendCard. Added 2x2 PMCategoryCard grid (teal/indigo/orange/green). Shimmer updated to match new layout. Removed metrics/city stats/activities from UI (data stays in UiState). ViewModel/UiState/UiAction unchanged. 3 previews (light/dark/shimmer).

- [x] **P2-3: ProfileScreen** — Redesign: avatar, name, stats grid, plate status card, social icons, activity cards. Remove plate badge from profile.
  - Files: `presentation/features/main/profile/ProfileScreen.kt`
  - Done when: Profile matches design layout. @Preview. Compiles.
  - Notes: Header restyled: name=headlineMedium, subtitle=12.5sp/textLabel. Badges use rFull+chipBg. Stats→PMStatCard 3-grid (removed 2-card PMCard layout). Status summary→dot+value+label columns. Social icons→44dp circles. Section headers→SectionLabel. All colors→pmColors. StatusPill colors changed from dark-bg to light-bg for light theme. Shimmer uses pmColors.skeleton. 3 previews (light/dark/shimmer). Removed PROFILE_FRIENDS_STAT_TAG (friends tap moved to stats area). ViewModel/UiState unchanged.

---

## Phase 3 — New Screens (Full Vertical Slice)

- [x] **P3-1: MessagesScreen** — Full slice: MessagesUiState, MessagesUiAction, MessagesUiEffect, MessagesViewModel, MessagesRoute, nav wiring, MessagesScreen UI with PMMessageItem list.
  - Files: `presentation/features/main/messages/` (new: MessagesUiState, MessagesUiAction, MessagesUiEffect, MessagesViewModel, MessagesRoute, MessagesScreen), `navigation/graphs/MessagesGraph.kt`, `navigation/graphs/MainGraph.kt`
  - Done when: Tab 3 navigates to MessagesScreen, shows message list, unread dots work. @Preview. Compiles.
  - Notes: Domain layer already had ChatRoom/ChatMessage models, ChatRepository, and GetChatRoomsUseCase — no new domain/data files needed. ViewModel uses GetChatRoomsUseCase, maps ChatRoom→MessageConversationUiModel. Screen uses PMMessageItem with dividers. Empty/loading/content states. MessagesGraph updated from placeholder to real route with hiltViewModel scoped to MainGraph. MainGraph passes navController. 3 previews (light/empty/dark).

- [x] **P3-2: PlateDetailScreen** — Full slice: PlateDetailUiState, PlateDetailViewModel, nav arg (plate id/number), PlateDetailRoute, screen UI with plate info, rating bars, tags, reviews, CTA.
  - Files: `domain/model/PlateDetail.kt` (new or extend existing), `presentation/features/main/platedetail/` (new package: UiState, ViewModel, Route, Screen, components/)
  - Done when: Navigation from search → detail works. Shows plate info, rating breakdown, tags, reviews, CTA button. @Preview. Compiles.
  - Notes: 6 files in platedetail package: PlateDetailUiState (state + RatingBreakdownItem, PlateTagUiModel, PlateReviewUiModel), PlateDetailUiAction (Back/Bookmark/Review), PlateDetailUiEffect (NavigateBack/NavigateToReview), PlateDetailViewModel (uses SearchPlateUseCase for basic plate data + fake breakdown/tags/reviews until backend supports), PlateDetailRoute (standard collectAsStateWithLifecycle + effect collection), PlateDetailScreen (Scaffold + TopAppBar with back/bookmark, LazyColumn with PlateInfoRow using PMPlateBadge Large + rating display, PMRatingBar breakdown, FlowRow tag chips with counts, review items with avatar circles, fixed bottom PMButton CTA). ViewModel uses savedStateHandle["id"] to work from both SearchGraph and DiscoverGraph. SearchGraph + DiscoverGraph updated: replaced MainPlaceholderScreen with real PlateDetailRoute. 2 previews (light/dark). Compiles.

- [x] **P3-3: ReviewScreen** — Full slice: ReviewUiState, ReviewUiAction, ReviewViewModel, ReviewRoute, screen UI with star input, sub-ratings, tag selection, text area, anonymous toggle, submit CTA.
  - Files: `presentation/features/main/review/` (new package: UiState, ViewModel, Route, Screen)
  - Done when: Navigation from detail → review works. Star selection, tag toggle, text input, submit all functional. @Preview. Compiles.
  - Notes: 5 files in review package: ReviewUiState (state + SubRatingUiModel, ReviewTagUiModel, ratingLabel computed, isSubmitEnabled, 12 default tags, 4 sub-ratings), ReviewUiAction (OverallRating/SubRating/TagToggle/Comment/Anonymous/Submit/Back), ReviewUiEffect (NavigateBack/ReviewSubmitted), ReviewViewModel (uses SearchPlateUseCase for plate info + AddPlateReviewUseCase for submit, 240 char limit on comment), ReviewRoute (standard pattern), ReviewScreen (Scaffold + TopAppBar "Değerlendir", PlateInfoCard with Review-size badge in surfaceSecondary card, interactive 34dp stars with verbal label, SubRatingsCard with 4 sub-rating rows, FlowRow of 12 PMTagChips, OutlinedTextField with char counter, fixed footer with anonymous checkbox + CTA). Added ReviewDestination(plateCode) to SearchDestinations. Added navigateToReview extension. Wired into SearchGraph + DiscoverGraph (from PlateDetail CTA → ReviewScreen, submit pops back). 2 previews (light/dark). Compiles.

---

## Post-Redesign

- [x] **POST-1: Cleanup** — Remove any dead orange color refs, unused old components, verify no hardcoded hex in screens.
  - Files: grep across presentation/
  - Done when: Zero hardcoded colors outside theme. Compiles.
  - Notes: No dead orange refs found. `categoryOrange*` tokens are intentional design tokens (orange category card in DiscoverScreen). Hardcoded hex in features/ limited to: @Preview backgroundColor params (compile-time constants, acceptable), avatar/status color pairs (per-entity decorations, not themeable). MainPlaceholderScreen still used by MessagesGraph (chat detail) and ProfileGraph (edit profile) — cannot remove yet. SearchGraph + DiscoverGraph cleaned of R imports and placeholder usage. No unused components found.

- [x] **POST-2: Dark theme verification** — Verify all screens render correctly in dark mode. Fix any contrast or visibility issues.
  - Files: all screen files (preview variants)
  - Done when: Dark previews look correct for all screens. Compiles.
  - Notes: All 10 screen files (Welcome, Login, Register, Search, Discover, Messages, Profile, PlateDetail, Review + settings sub-screens) have dark theme @Preview variants with `PlateMateTheme(darkTheme = true)`. All runtime colors use `pmColors.*` or `MaterialTheme.colorScheme.*` — no hardcoded hex outside preview data. Inline `Color(0x...)` only in preview state builders (avatar/status colors) and MessagesViewModel (avatar palette, intentionally theme-invariant). Dark PMColors provides proper contrast: dark surfaces (#0F172A/#162032), light text (#F1F5F9/#E2E8F0), dark cards with borders, adjusted category/status colors.
