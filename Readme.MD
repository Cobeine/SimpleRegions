## API examples

#### Custom flag 
```java
    public class CustomFlag extends BasicRegionFlag<Event> {
        public CustomFlag() {
            super(name);
        }

        @Override
        public void onHandle(Event event) {
            BukkitRegion region = getRegion(location);
            switch (region.getFlagSetting(this)) {
                case NOT_SET -> 
                case NONE -> 
                case WHITELIST ->
                case EVERYONE ->
            }
        }
    }
```
#### Registering your custom flag
```java
    SimpleRegions.getInstance().getRegionManager().registerFlag(new CustomFlag());
```

