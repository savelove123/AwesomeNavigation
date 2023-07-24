# AwesomeNavigation
 基于依赖注入的Android导航页面

相关文章：http://t.csdn.cn/AMgGT

# 导航框架功能定义 

- 支持每个功能组件导航的时候不直接依赖其他功能组件的具体实现
- 每个功能组件声明一个接口模块，这个接口模块描述了它声明了一组期望的目的地，实现了这个接口- 模块的功能组件导航到目的地。
- 每个功能组件自己实现导航跳转的逻辑，不是在app模块实现，也不是在公共模块实现
- 每个功能组件的实现允许依赖多个不同组件的接口模块
- 导航到目的地的具体行为由功能实现组件内部维护，对外不可见
- 导航的行为是一个抽象的行为，并且和清洁架构中的domain层解耦，导航不需要关注Domain层次的信息，只需要关注输入的参数信息，然后根据参数做出正确的行为即可。
- 导航支持的目的地是可以灵活扩展的：Activity,Fragment,Dialog
- 支持使用协程获取结果。
- 只依赖官方的组件，不需要额外插件

# 导航框架的元素
基于导航的概念，我定义了框架中的四个基本元素

- NavComponent 导航的逻辑组件，通常是ViewModel
- Navigable 导航的载体,通常是Activity,Fragment
- NavDestination 导航的目标点，可能是个Intent，Fragment,Dialog...
- NavCommand 导航的行为 navigateTo,back,finish,toPrevious    

# 使用

### 声明导航节点


```kotin
interface AuthDestination:ScreenDestination{

    @Parcelize
    data class Params(private val authType:AuthType, val phone:String?=null):Parcelable{
        val type :AuthType get() = run {
            if (authType != AuthType.Login && authType != AuthType.Register && phone.isNullOrEmpty()) {
                AuthType.Login
            } else {
                authType
            }
        }
    }
}
```

### 实现导航节点

```kotlin
class AuthDestinationImpl: AuthDestination{
    override fun toScreen( params: Parcelable?, navigable: Navigable): Screen {
        return Intent(navigable.toContext(), AuthActivity::class.java)
    }
}
```

###声明导航组件

```kotlin
@HiltViewModel
class SomeViewModel @Inject constructor( authDesitination:AuthDesination ):NavViewModel<AuthDesination>( application = application )
```

### 执行导航
```
authDesitination.injectParams( AuthDestination.Params( AuthType.Login ) )
```

### 在目标页面注入参数
```kotlin
 @AndroidEntryPoint
class AuthActivity: BaseActivity<ActivityAuthBinding,AuthViewModel>() ,Navigable{

    private val authViewModel :AuthViewModel by lazyNavViewModel {
        observeNavigation()
        injectVMParams(requireNavParam())
    }
```

### ViewModel获取导航参数
```kotlin
@HiltViewModel
class AuthViewModel @Inject constructor( userInfoDestination:UseInfoDestination ):NavViewModel<AuthNextDestination>( application = application ),Parameterized<AuthDestination.Params>{

    fun attachViewModel( saveInstanceState:Bundle? ){
        if(requireVMParams().type == AuthType.Register ){
            userInfoDestination.navigate()
        }
    }
}
```




