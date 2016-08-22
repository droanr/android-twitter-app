The following user stories must be completed:

- [x] User can sign in to Twitter using OAuth login (2 points)
- [x] User can view the tweets from their home timeline
    - User should be displayed the username, name, and body for each tweet (2 points)
    - User should be displayed the relative timestamp for each tweet "8m", "7h" (1 point)
    - User can view more tweets as they scroll with infinite pagination (1 point)
- [x] User can compose a new tweet
    - User can click a “Compose” icon in the Action Bar on the top right (1 point)
    - User can then enter a new tweet and post this to twitter (2 points)
    - User is taken back to home timeline with new tweet visible in timeline (1 point)

The following advanced user stories are optional:
- [x] While composing a tweet, user can see a character counter with characters remaining for tweet out of 140 (1 point)
- [x] Links in tweets are clickable and will launch the web browser (see autolink) (1 point)
- [x] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh) (1 point)
- [ ] User can open the twitter app offline and see last loaded tweets
    - Tweets are persisted into sqlite and can be displayed from the local DB (2 points)
- [x] User can tap a tweet to display a "detailed" view of that tweet (2 points)
- [ ] User can select "reply" from detail view to respond to a tweet (1 point)
- [ ] Improve the user interface and theme the app to feel "twitter branded" (1 to 5 points)
- [x] Stretch: User can see embedded image media within the tweet detail view (1 point)
- [ ] Stretch: User can watch embedded video within the tweet (1 point)
- [x] Stretch: Compose activity is replaced with a modal overlay (2 points)
- [x] Stretch: Use Parcelable instead of Serializable using the popular Parceler library. (1 point)
- [x] Stretch: Apply the popular Butterknife annotation library to reduce view boilerplate. (1 point)
- [x] Stretch: Leverage RecyclerView as a replacement for the ListView and ArrayAdapter for all lists of tweets. (2 points)
- [ ] Stretch: Move the "Compose" action to a FloatingActionButton instead of on the AppBar. (1 point)
- [ ] Stretch: Replace all icon drawables and other static image assets with vector drawables where appropriate. (1 point)
- [ ] Stretch: Leverage the data binding support module to bind data into one or more activity or fragment layout templates. (1 point)
- [x] Stretch: Replace Picasso with Glide for more efficient image rendering. (1 point)

The following ADDITIONAL features are implemented:
- [x] Add ability to retweet, and show retweet count in timeline view.

