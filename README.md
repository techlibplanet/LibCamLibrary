# LibCamLibrary
[![](https://jitpack.io/v/techlibplanet/LibCamLibrary.svg)](https://jitpack.io/#techlibplanet/LibCamLibrary)

## To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

### Gradle
```kotlin
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  
  
Step 2. Add the dependency
  
  ```kotlin
  dependencies {
	        implementation 'com.github.techlibplanet:LibCamLibrary:1.0.0'
	}
  ```
  
  ### Maven
  
  Step 1. Add the JitPack repository to your build file
  
  ```kotlin
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  
  Step 2. Add the dependency
    
```kotlin
    <dependency>
	<groupId>com.github.techlibplanet</groupId>
	<artifactId>LibCamLibrary</artifactId>
	<version>1.0.0</version>
    </dependency>	
```
    
