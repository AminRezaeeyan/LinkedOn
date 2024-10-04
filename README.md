# LinkedOn

LinkedOn is a JavaFX-based desktop application that mimics the functionality of LinkedIn. It features a beautiful user interface with both light and dark themes. Users can sign up, log in, connect with others, share posts, and much more.

## Table of Contents

- [Features](#Features)
- [Screenshots](#Screenshots)
- [Technologies Used](#Technologies%20Used)
- [Setup](#Setup)
- [Configuration](#Configuration)
- [Endpoints](#Endpoints)
- [Contribution](#Contribution)
- [License](#License)

## Features

- User authentication (sign up, log in)
- Messaging system
- Connect and follow users
- Share and like posts
- Edit profile and contact information
- Add education and skills
- Search for users
- Secure password hashing and unique ID generation
- Light and dark themes
- Optimized database queries and using indexes for better performance

## Screenshots

| viewing your own profile | viewing other's profiles |
|:------:|:-------:|
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/16.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/20.png) |

| landing page | sign up | sign in|
|:------:|:-------:|:-------:|
|![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/1.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/4.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/3.png) |
|![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/2.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/5.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/6.png) |

| editting profile | adding educations and skills |
|:------:|:-------:|
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/19.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/18.png) |
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/17.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/7.png)  |

| home page | followers and connections |
|:------:|:-------:|
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/12.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/14.png) |
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/13.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/11.png) |

| invitations | searching users |
|:------:|:-------:|
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/10.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/15.png) |

| messaging | educations and skills |
|:------:|:-------:|
| ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/9.png) | ![demo](https://github.com/AminRezaeeyan/LinkedOn/blob/main/Screenshots/8.png)  |

## Technologies Used

- Java
- JavaFX
- MySQL
- JWT (JSON Web Tokens)
- JSON
- HTTP Server
- MVC Design Pattern
- Singleton and DAO Design Patterns
- BCrypt for password hashing
- UUID for unique ID generation

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AminRezaeeyan/LinkedOn.git
   cd LinkedOn
   ```
2. **Set up the database:**

    Import the `databaseSetUp.sql` file located in the `resources` folder to your MySQL server. This file includes all necessary scripts for setting up tables, triggers, procedures, indexes, and feed data.
    Update the `app.properties` file with your database configuration. 

3. **Run the application:**
   
   Open the project in your preferred IDE.
   Build and run the project.

## Configuration

- Edit the `app.properties` file located in the `resources` folder of server side to change the configuration.

```java
# <<<------<<    App Information    >>------>>>
app.name=LinkedOn
app.author=Amin Rezaeeyan
app.version=1.0.0

# <<<------<<    Database Configuration   >>------>>>
db.url=jdbc:mysql://localhost:3306/LinkedOn_DB
db.username=LinkedOn
db.password=LinkedOnApp2024
db.driver=com.mysql.cj.jdbc.Driver

# <<<------<<    JWT Configuration   >>------>>>
jwt.secret=S3cReT#!KeY$&Of*)OuR^(PrOjEcT%~
jwt.issuer=org.LinkedON
```

## Endpoints

### Users
- `api/users`
- `api/users/:user-id?detail=full`
- `api/users/:user-id?detail=brief`
- `api/users?searchQuery=:searchQuery`
- `api/users?checkEmail?email=:email`
- `api/users/login?email=:email&password=:password`

### User Feed
- `api/users/:user-id/feed`

### Contact Info
- `api/users/:user-id/contactInfo`

### Educations
- `api/users/:user-id/educations`
- `api/educations`
- `api/educations/:education-id`

### Skills
- `api/users/:user-id/skills`

### Following
- `api/follows/:user-id/followers`
- `api/follows/:user-id/followings`
- `api/follow/:followed-user-id/followers/:following-user-id`

### Connection
- `api/connects/:user-id`
- `api/connects/:user-id/:user-id`
- `api.connects/:user-id/requests`
- `api/connects/:user-id/request/:user-id`
- `api/connects/:user-id/accept/:user-id`

### Posts
- `api/posts/:post-id`
- `api/posts/:post-id/likes`
- `api/posts/:post-id/likes/:user-id`

### Messages
- `api/messages/:user-id/:user-id`
- `api/messages/:sender-id/:receiver-id`

### Media
- `api/media/users/:user-id/:media-name/:media-extension`
- `api/media/posts/:post-id/:media-name/:media-extension`

## Contribution

Contributions are welcome! Please fork the repository and submit a pull request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.
