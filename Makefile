make desktop, Html, jar:

desktop:
	./gradlew lwjgl3:run --warning-mode all

Html:
	./gradlew html:superDev

jar:
	./gradlew lwjgl3:dist

android:
	gradlew android:assembleRelease