ConfigurationBuilder builder = new ConfigurationBuilder();
builder.statistics().enable()
         //Register JMX MBeans for the Cache Manager and remote caches.
         .jmxEnable()
         //Set the JMX domain name to which MBeans are exposed.
         .jmxDomain("org.example")
       .addServer()
         .host("127.0.0.1")
         .port(11222);
RemoteCacheManager remoteCacheManager = new RemoteCacheManager(builder.build());
