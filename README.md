![REPL session](revolt.gif?raw=true "session")

This is a sample project making use of [defunkt/revolt](https://github.com/mbuczko/revolt) library.

All the magic happens in `deps.edn` which contains a few _dev_ aliases with extra-dependencies required by certain plugins.

* `:dev` - general development dependencies (revolt as dependency goes here)
* `:dev/nrepl` - nrepl and cider-related dependencies.
* `:dev/cljs` - clojurescript related dependencies along with all clojurescript libs used in project.
* `:dev/pack` - packaging dependencies (capsule).

Now, depending on needs some or all of theses aliases may be used by `clj` tool with `-A` parameter.

Here are some examples:

### activate nrepl plugin only (eg. to be able to connect from Cider)
``` sh
clj -A:dev:dev/nrepl -p nrepl
```

### clean target dir and activate rebel
``` sh
clj -A:dev:dev/cljs -p rebel -t clean
```

### activate filesystem watcher, nRepl, figwheel, rebel plugins, regenerate CSSes and initialize tests
``` sh
clj -A:dev:dev/nrepl:dev/cljs -p watch,nrepl,figwheel,rebel -t clean,sass,test
```

### build a codox documentation (run a codox task)
``` sh
clj -A:dev:dev/cljs -t codox:name=foo:version=1.2.2
```

### build a codox documentation based on preconfigured project information
``` sh
clj -A:dev:dev/cljs -t info,codox
```

### build a fat, aot-ed capsule - aka "all deps included" (run clean, info, sass, cljs aot and capsule tasks)
``` sh
clj -A:dev:dev/cljs:dev/pack -t clean,info,aot,sass,cljs:compiler.optimizations=advanced,assets,capsule
```

### build a thin capsule (run clean, info, sass, cljs and capsule tasks)
``` sh
clj -A:dev:dev/cljs:dev/pack -t clean,info,sass,cljs:compiler.optimizations=advanced,assets,capsule:capsule-type=thin
```



