# Оповіщення про тривогу (Android)

Простий Android-додаток на Kotlin із чотирма кнопками для послідовного запуску сповіщень та звукових сигналів:

1. **Повітряна тривога** — системне сповіщення + звук сирени (`air_raid_alert.wav`).
2. **Підвищений рівень** — системне сповіщення + сигнал підвищеної небезпеки (`elevated_alert.wav`).
3. **Відбій повітряної тривоги** — системне сповіщення + сигнал відбою (`all_clear_alert.wav`).
4. **Хвилина мовчання** — системне сповіщення + голосове оголошення українською через `TextToSpeech` (*«Увага, загальнонаціональна хвилина мовчання.»*) + 60-секундний звук метронома (`minute_of_silence_metronome.wav`).

Повторне натискання на активну кнопку зупиняє відтворення; натискання іншої кнопки миттєво перемикає сигнал.

## Як відкрити та зібрати APK

1. Розпакуйте архів і відкрийте папку `AlertNotifierApp` в **Android Studio** (Hedgehog або новіше).
2. Дочекайтеся синхронізації Gradle.
3. Підключіть Android-смартфон (Android 8.0 / API 26+) або запустіть емулятор і натисніть **Run** (або **Build -> Build Bundle(s) / APK(s) -> Build APK(s)**).
4. На Android 13+ дозвольте додатку надсилати сповіщення (`POST_NOTIFICATIONS`) при першому запуску.
5. Для озвучення фрази *«Увага, загальнонаціональна хвилина мовчання»* на пристрої має бути встановлений український мовний пакет Google Speech Services (зазвичай активний за замовчуванням).

## Заміна звуків на власні MP3 з інтернету

У проєкт уже додано готові синтезовані `.wav`-файли в папці `app/src/main/res/raw/`, тому додаток працює одразу без інтернету. За бажання ви можете замінити їх на будь-які завантажені MP3-файли, зберігши ті самі базові назви (змінювати код не потрібно):

- `app/src/main/res/raw/air_raid_alert.mp3` — наприклад, із [FreeTone (Сигнал повітряної тривоги)](https://freetone.org/ringtoni/zvukovi-efekti/signal-povitryanoyi-trivogi) або [Directory.Audio (Сигнал повітряної тривоги в Києві)](https://directory.audio/ru/zvukovye-effekty/budilniki-i-signaly/34781-signal-vozdushnoj-trevogi-v-kieve-polnaya-versiya)
- `app/src/main/res/raw/elevated_alert.mp3` — наприклад, із добірки [ZvukoGram (Звуки тривоги)](https://zvukogram.com/category/zvuki-trevogi/)
- `app/src/main/res/raw/all_clear_alert.mp3` — наприклад, із [ZvukiPro (Звуки сирени)](https://zvukipro.com/situacii/2416-zvuki-sireny-vozdushnoj-trevogi.html)
- `app/src/main/res/raw/minute_of_silence_metronome.mp3` — наприклад, із [ZvukiPro (Звук метронома — хвилина мовчання)](https://zvukipro.com/situacii/201-zvuk-metronoma-minuty-molchaniya.html) або аудіо з [офіційного запису МОН України на YouTube](https://www.youtube.com/watch?v=yYKHuAEhjBU)

> Важливо: видаліть відповідний `.wav`-файл, якщо додаєте `.mp3` з такою самою назвою, щоб уникнути дублювання ресурсів у `res/raw/`.
## Автоматична збірка APK через GitHub Actions (без Android Studio)

1. Створіть новий репозиторій на [GitHub](https://github.com/new).
2. Розпакуйте архів і завантажте **вміст** папки `AlertNotifierApp` у корінь репозиторію (переконайтеся, що папка `.github/workflows/build-apk.yml` також завантажена).
3. Відкрийте вкладку **Actions** у репозиторії — збірка **Build Android APK** запуститься автоматично (або натисніть **Run workflow**).
4. Після завершення збірки відкрийте виконаний запуск і завантажте архів `AlertNotifierApp-debug-apk` із розділу **Artifacts** — усередині буде готовий файл `app-debug.apk`.
