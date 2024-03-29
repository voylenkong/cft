Тестовое задание на стажировку в ШИФТ ЦФТ.
---
---
Программа сортировки слиянием нескольких файлов. Предполагается, что файлы предварительно отсортированы.
Входные файлы содержат данные одного из двух видов: целые числа или строки. Данные записаны
в столбик (каждая строка файла – новый элемент). Строки могут содержать любые не пробельные
символы, строки с пробелами считаются ошибочными.

Результатом работы программы должен являться новый файл с объединенным содержимым
входных файлов, отсортированным по возрастанию или убыванию путем сортировки слиянием.

Уточнения запуска программы:
1. Имя выходного файла должно идти первым, затем имена входных файлов. out.txt идет раньше in.txt.
   Например: java -jar demoShift.jar -i -a out.txt in.txt
2. Можно не делать перечисление входных файлов, а указать каталог.
   Например: java -jar demoShift.jar -i -a tmp\
   В случае если во входном наборе файлов первым идет каталог остальные входные файлы отбрасываются (in2.txt in3.txt)
3. Имеются допоплнительные ключи -f и -b.
   Ключ -f указывает максимальный размер файла (в байтах). Максимальный размер файла - это размер, до размера которого не будет происходить его фрагментация.
   Ключ -b указывает максимальный размер буфера (в строках). Максимальный размер буфера - это размер фрагмента при фрагментации.
   Например: пришли на вход два файла объемом 10Gb. Объем оперативной памяти не позволит провести его сортировку.
   Опцией -f укажем размер файла до которого не будем проводить фрагментацию (-f 1073741824).
   Опцией -b укажем размер фрагмента файла (-b 50000000).
   Таким образом наш входной файл разобъем на фрагменты.
---

Информация по технологиям:
---
1. Maven 3.8.1
2. Project Lombok
3. Apache Commons CLI
4. Apache Commons IO