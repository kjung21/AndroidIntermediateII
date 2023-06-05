# Quiz: Offline Cache

## Which statements are true about using a database for an offline cache?

```
1. It's OK to skip the database when showing data, I can always save it later.
2. Storing the latest result in the database means the offline cache is always up to date.
3. Always displaying from the database exposes issues stale cache data during development.
4. When updating values, make sure the server responds successfully before saving them in the offline cache.

Options 2, 3, and 4 are true.
```

## When should you prefer LiveData in Room @Queries?

```
1. You want to watch for changes to the database and update the UI dynamically.
2. You want to be able to call the query from the UI thread.
3. You want Room to run the query to run on a background thread.
4. You only need to run the query once, and dont' care about future changes.
5. You will always call this query from a background thread.

You should prefer LiveData in Room @Queries for options 1, 2, and 3.
```

## How can you avoid using too much battery in the background?

```
Use WorkManager.
Specify as many reasonable constraints as possible for each job, such as device idle, charging, and nconnected to wifi.
Be sparing about scheduling background work - if the work can wait until next launch then do it when the app is in the foreground.
When scheduling work that happens periodically, use the largest delay possible between each run.
Make sure your background jobs are efficient and don't use excessive network, CPU, or disk access.

All of these are good ways to avoid using too much battery in the background.
```

## Overview

```
Data stored in the database should be used as a backup for display.
True
False   CHECK

An abstraction layer providing data access methods that hide the source from the caller is a..
Repository      CHECK
Database
Domain
None of the above

The general computer science term representing data stored for future is use is __________.
Memory
Cache   CHECK
Disk storage
None of the above

An abstraction layer that provides a controlled model for data provided from a network source is..
Repository
Database
Domain      CHECK
None of the above

The Android API that handles background operations at a system-level outside of the application lifecycle.
Work
WorkManager     CHECK
Cache
None of the above

The ________ method of a Worker defines the specific tasks to be run as part of a background operation.
doWork()    CHECK
doInBackground()
onBackground()
None of the above

Background operations handled by Android will run at precise intervals defined as part of the PeriodicWork.
True
False   CHECK

A _________ prevents background tasks from being run while the device is in active use.
Filter
Constraint      CHECK
Rule
None of the above
```

