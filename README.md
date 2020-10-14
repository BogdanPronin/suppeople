# Suppeople - приложение команды BS corporation 
Сервис использует протокол HTTP и формат JSON для передачи данных, авторизация Basic Auth
##### Сервис имеет интерфейс для администратора и для пользователя
> ### Интервейс для пользователя:
>> #### CRUD User:
>> - `localhost:22867/users` **(post)** - создает нового пользователя. Автоматически дает роль *USER*. **Важно**, все дальнейшие действия может проводить ТОЛЬКО авторизованный пользователь
>>>> - Пример успешного запроса:
>>>> - {
>>>> - "fname":"John",
>>>> - "lname":"Doe",
>>>> - "login":"JohnDoe",
>>>> - "password":"johndoe",
>>>> - "phone":"88888888888",
>>>> - "email":"JohnDoe@gmail.com",
>>>> - "dateOfBirthday":"2000-08-08",
>>>> - "city":"1"
>>>> - }
>>>> - `localhost:22867/users` **(get)** - возвращает всех пользователей.

>> - `localhost:22867/users`