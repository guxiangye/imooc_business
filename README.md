# imooc_music_app

 组件化架构实现慕课云音乐app

# 依赖方式
implementation:
该依赖方式所依赖的库不会传递，只会在当前module中生效。
只能在内部使用此模块，比如我在一个libiary中使用implementation依赖了gson库，
然后我的主项目依赖了libiary，那么，我的主项目就无法访问gson库中的方法。这样的好处是编译速度会加快，
推荐使用implementation的方式去依赖，如果你需要提供给外部访问，那么就使用api依赖即可

api:
跟2.x版本的 compile完全相同
该依赖方式会传递所依赖的库，当其他module依赖了该module时，可以使用该module下使用api依赖的库。

provided（compileOnly）:
只在编译时有效，不会参与打包
可以在自己的moudle中使用该方式依赖一些比如com.android.support，gson这些使用者常用的库，避免冲突。

apk（runtimeOnly）:
只在生成apk的时候参与打包，编译时不会参与，很少用。

testCompile（testImplementation）:
testCompile 只在单元测试代码的编译以及最终打包测试apk时有效。

debugCompile（debugImplementation）:
debugCompile 只在debug模式的编译和最终的debug apk打包时有效

releaseCompile（releaseImplementation）:
Release compile 仅仅针对Release 模式的编译和最终的Release apk打包。