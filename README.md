### Усложнение 1. Анализ производительности и памяти

#### CPU Profiler
- Задача `Find CPU Hotspots (Java/Kotlin Method Recording)`.
- `MainActivity.onCreate` — ~103 мс Total, из них **Self = 0,135 мс**
  (~1% от общего времени записи).
- Тяжёлых циклов, блокировок CPU нет.

#### Heap Dump
- Задача `Analyze Memory Usage (Heap Dump)`.
- В `References` для `MainActivity` — только системные ссылки
  (`ActivityThread$ActivityClientRecord`, `PhoneWindow`, `WeakReference`).
- Ссылок из пользовательских синглтонов нет — утечек не обнаружено.

Скриншоты:

![onCreate до усложнений](screenshots/u1-cpu-oncreate.png)

![процесс до усложнений](screenshots/u1-baseline-processes.jpg)

![память до усложнений](screenshots/u1-baseline-memory.jpg)

## Усложнение 2: намеренные утечка и тормоза

### Часть A. Утечка памяти

#### Симптом

После 5 поворотов экрана `dumpsys meminfo` показывает 5–6 живых Activity.
Число **не уменьшается** через 10 секунд — GC не может собрать.

![процессы после утечки](screenshots/u2-fixed-processes.jpg)

![oncreate после утечки](screenshots/u2-cpu-after-leak.png)

В heap dump видно 5+ экземпляров `MainActivity`:

![память после утечки](screenshots/u2-fixed-memory.jpg)

#### Решение

Убрал `LeakHolder.leaked.add(this)`. После починки:

-  После 5 поворотов: `Activities: 1`.

### Часть B. Тормоз

#### Симптом

В `MainActivity.onCreate` добавлен тяжёлый цикл на 500M итераций — он
блокирует главный поток на несколько секунд.

В Logcat видно фактическое время выполнения цикла:

![Logcat — тяжёлый цикл](screenshots/u2-slow-logcat.png)

CPU Profiler показывает, что `MainActivity.onCreate` занимает тысячи
миллисекунд `Self`-времени; на Flame Chart — широкий «столб» в `onCreate`:

![CPU Profiler — onCreate с циклом](screenshots/u2-cpu-fixed.png)

#### Решение

Убрал цикл из `onCreate`. Правильное решение для реального кода —
вынести работу в `Dispatchers.Default` через корутины (модуль про
многопоточность). После починки `onCreate` Self снова < 1 мс
