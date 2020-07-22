OpenBlocker - порт мода XBlocker с версии 1.7.10 на 1.12, позволяющий администратору быстро заблокировать взаимодействие, крафт и т.п. для блока

# Возможности:
 * Полная блокировка блока/предмета(невозможно ни подобрать, ни скрафтить, никак положить в инвентарь)
 
   (^ Специально реализованная блокировка для предметов, имеющие разные NBT,но одинаковые названия и метадаты.)
 * Блокировка крафта
 * Установка максимального кол-ва блоков в чанке
 * Блокировка определённого зачарования для игрока
 * Отдельное Гуи для игроков, в котором можно посмотреть информацию о предмете (а так-же причину блокировки, если имеется)
 * Возможность блокировки целыми метадатами (избавляет от надобности вводить по 100500 раз одну и ту же команду, меняя 1-2 цифры.)
 * Система хранения в json файлах позволяет даже при выключенном сервере подредактировать заблокированные предметы.
 * Автоматическая синхронизация конфига блокировок с клиентом (При заходе игрок автоматически получит всё,что необходимо для работы)
# Использование:
 https://gitlab.com/Will0376/openblocker/-/wikis/Использование
# Известные баги:
 * Furniture Mod добавляет синхронизацию рецептов с сервера на клиент (Не работает удаление рецептов)
 * ~~IC2: Если заблокировать 1 машинку из мода - блокируются все машинки.~~ - Фикс есть, нужны тесты
# От автора:
Если вы нашли блок, который не блокируется - милости прошу в обсуждения: https://gitlab.com/Will0376/openblocker/-/issues/new