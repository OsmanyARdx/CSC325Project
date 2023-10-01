<?php

$is_invalid = false;
$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$db = "Mental";
$conn = new mysqli($dbhost, $dbuser, $dbpass, $db);

if ($_SERVER["REQUEST_METHOD"] === "POST") {

    $sql = sprintf(
        "SELECT * FROM user
                    WHERE email = '%s'",
        $conn->real_escape_string($_POST["email"])
    );

    $result = $conn->query($sql);

    $user = $result->fetch_assoc();

    if ($user) {

        if (password_verify($_POST["password"], $user["password_hash"])) {

            session_start();

            session_regenerate_id();

            $_SESSION["user_id"] = $user["userid"];

            header("Location: index.php");

            exit;
        }
    }

    $is_invalid = true;
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="logincss.css">
</head>

<body>
    <div class="container">
        <div class="left">
            <div class="header">
                <h2 class="animation a1">Welcome Back</h2>
                <h4 class="animation a2">Log in to your account using email and password</h4>
            </div>
            <div class="form">
                <input type="email" class="form-field animation a3" placeholder="Email Address">
                <input type="password" class="form-field animation a4" placeholder="Password">
                <p class="animation a5"><a href="#">Forgot Password</a></p>
                <button class="animation a6">LOGIN</button>
            </div>
        </div>
        <div class="right"></div>
    </div>

</body>

</html>