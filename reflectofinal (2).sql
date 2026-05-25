-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 22, 2025 at 03:04 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `reflectofinal`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `addNewChallenge` (IN `uid` INT(255), IN `cDesc` VARCHAR(255))   BEGIN
INSERT INTO challenges_table(user_ID,challenge_desc,accepted,accomplished) VALUES(uid,cDesc,'No','No');
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `addPost` (IN `pid` INT, IN `uid` INT, IN `title` VARCHAR(500), IN `pdata` VARCHAR(15000), IN `fld` VARCHAR(30), IN `l` INT, IN `ts` TIMESTAMP)   BEGIN
INSERT INTO posts VALUES (pid,uid,title,pdata,fld,l,ts);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `checkPasscode` (IN `unm` VARCHAR(60), OUT `pass` VARCHAR(30))   BEGIN
SELECT passcode into pass from users where username=unm;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delChallenge` (IN `cid` INT(255), IN `uid` INT)   BEGIN
DELETE FROM challenges_table WHERE user_ID=uid AND challenge_ID=cid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteUserPost` (IN `pid` INT)   BEGIN
DELETE FROM posts WHERE post_id=pid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAccomplishmentsByUserID` (IN `uid` INT(255))   BEGIN
SELECT * FROM challenges_table WHERE user_ID=uid AND accomplished='Yes';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAccomplishmentsCountByUserID` (IN `uid` INT(255))   BEGIN
SELECT COUNT(*) FROM challenges_table WHERE user_ID=uid AND accomplished='Yes';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllChallenges` ()   BEGIN
SELECT * FROM challenges_table;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllChallengesByUserID` (IN `uid` INT(255))   BEGIN
SELECT * FROM challenges_table WHERE user_ID=uid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllChallengesByUserName` (IN `uName` VARCHAR(255))   BEGIN
SELECT challenges_table.user_ID,challenges_table.challenge_ID,challenges_table.challenge_desc,challenges_table.accepted,challenges_table.accomplished FROM challenges_table INNER JOIN user ON users.user_ID=challenges_table.user_ID WHERE users.username=uName;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getChallengeByCID` (IN `cid` INT(255))   BEGIN
SELECT * FROM challenges_table WHERE challenge_ID=cid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getChallengesCount` ()   BEGIN
SELECT COUNT(*) FROM challenges_table;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getChallengesCountByUserID` (IN `uid` INT(255))   BEGIN
SELECT COUNT(*) FROM challenges_table WHERE user_ID=uid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getChallengesCountByUserName` (IN `uName` VARCHAR(255))   BEGIN
SELECT COUNT(*) FROM users INNER JOIN challenges_table ON users.user_ID=challenges_table.user_ID WHERE users.username=uName;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getDate` (OUT `dt` TIMESTAMP)   BEGIN
SELECT CURRENT_TIMESTAMP into dt;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getJournalData` (OUT `entry` VARCHAR(15000), IN `jtitle` VARCHAR(60), IN `uid` INT)   BEGIN
SELECT  journal_data into entry from journal where title=jtitle and User_id=uid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getNewPostId` ()   BEGIN
SELECT post_id+1 FROM posts ORDER BY post_id DESC LIMIT 1;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getNewUserId` ()   BEGIN
SELECT COUNT(*)+101 FROM users;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getPassword` (IN `uid` INT)   BEGIN
SELECT password FROM Users WHERE user_id=uid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getQuotes` ()   BEGIN
SELECT * FROM quotes;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserId` (OUT `uid` INT(60), IN `unm` VARCHAR(30), IN `pass` VARCHAR(30))   BEGIN
SELECT user_id into uid FROM Users WHERE username=unm AND passcode=pass;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserName` (IN `uid` INT)   BEGIN
SELECT username FROM Users WHERE user_id=uid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserPosts` (IN `uid` INT)   BEGIN
SELECT post_id,title,post_data,field,likes FROM posts WHERE user_id=uid ORDER BY date_time DESC;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertUser` (IN `uid` INT, IN `unm` VARCHAR(60), IN `pass` VARCHAR(30))   BEGIN
INSERT INTO users VALUES (uid,unm,pass);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `likePost` (IN `pid` INT)   BEGIN
UPDATE posts SET likes=likes+1 WHERE post_id=pid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `loginUser` (IN `unm` VARCHAR(30))   BEGIN
SELECT * FROM users where username=unm;
end$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `markAsAccomplished` (IN `cid` INT(255))   BEGIN
UPDATE challenges_table SET accomplished='Yes',accepted='Done' WHERE challenge_ID=cid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `markAsUnAccomplished` (IN `cid` INT(255))   BEGIN
UPDATE challenges_table SET accomplished='No' WHERE challenge_ID=cid AND accomplished='Yes';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `readLatestPost` ()   BEGIN
SELECT p.post_id,p.post_data,u.username,p.likes,p.field FROM posts p JOIN users u ON p.user_id=u.user_id ORDER BY date_time DESC LIMIT 1;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `readRandomPost` (IN `pid` INT)   BEGIN
SELECT p.post_data,u.username,p.likes,p.field FROM posts p JOIN users u ON p.user_id=u.user_id WHERE p.post_id=pid;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `challenges_table`
--

CREATE TABLE `challenges_table` (
  `user_ID` int(11) NOT NULL,
  `challenge_ID` int(11) NOT NULL,
  `challenge_desc` varchar(5000) NOT NULL,
  `accepted` varchar(30) NOT NULL,
  `accomplished` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `challenges_table`
--

INSERT INTO `challenges_table` (`user_ID`, `challenge_ID`, `challenge_desc`, `accepted`, `accomplished`) VALUES
(151, 2, 'Draw a portrait', 'Done', 'Yes'),
(151, 3, 'Hello there, again checking for exceptions.', 'No', 'No');

-- --------------------------------------------------------

--
-- Table structure for table `journal`
--

CREATE TABLE `journal` (
  `user_id` int(11) NOT NULL,
  `Title` varchar(60) NOT NULL,
  `journal_data` varchar(15000) NOT NULL,
  `journal_files` blob DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `journal`
--

INSERT INTO `journal` (`user_id`, `Title`, `journal_data`, `journal_files`, `date_time`) VALUES
(151, 'testingggg', '', '', '2025-07-24 15:25:52'),
(151, 'testinggg', 'codeeeee testinggggupdate case textingupdate case testing', '', '2025-07-25 17:41:09'),
(151, 'code export case testing', 'code export case testing', '', '2025-07-25 17:57:20'),
(153, 'code check', ' checking code for add journal entrychecking edit add data case hey', NULL, '2025-08-17 19:02:01'),
(154, 'codeChecking', 'java code checking hhhhh', NULL, '2025-08-17 22:02:30'),
(154, 'checkImportExport', 'checkinggg import export cases ', 0x636865636b496d706f72744578706f72740d0a323032352d30382d31382030333a31383a35352e300d0a636865636b696e6720696d706f7274206578706f7274206361736573, '2025-08-17 21:53:11'),
(151, 'finalChecking', 'abc\n xyz\n xyzzz1 ', 0x636865636b496d706f72744578706f72740d0a323032352d30382d31382030333a31383a35352e300d0a636865636b696e6720696d706f7274206578706f7274206361736573, '2025-08-18 14:27:10'),
(151, 'heyo', 'abc1. xyz. vvv. hhhh. ', NULL, '2025-08-20 14:50:40'),
(151, 'code testing new entry', 'Just checking the code . Nothing just adding data to the entry.', NULL, '2025-08-21 05:05:19');

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(500) NOT NULL,
  `post_data` varchar(15000) NOT NULL,
  `field` varchar(30) NOT NULL,
  `likes` int(11) NOT NULL,
  `date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`post_id`, `user_id`, `title`, `post_data`, `field`, `likes`, `date_time`) VALUES
(101, 121, 'Healing Is About Carrying, Not Forgetting', 'I’m starting to understand that healing is not about forgetting. It’s about learning how to carry things without letting them destroy you.', 'healing', 37, '2025-08-17 13:22:36'),
(102, 107, 'A Poem to My Younger Self', 'Wrote a poem today about my younger self. Made me tear up. So much I’ve buried that I didn’t even realize.', 'self-reflection', 8, '2025-05-30 17:44:00'),
(103, 114, 'Opening Up to Mom About My Feelings', 'Finally talked to mom about how I’ve been feeling. It was hard. But I think she needed to hear it too.', 'relationships', 3, '2025-07-01 09:39:00'),
(104, 125, 'Walking in the Rain With Just My Breath', 'Had a peaceful walk in the rain today. No music. Just the sound of water and my breath.', 'mindfulness', 27, '2025-07-10 01:51:00'),
(105, 133, 'A Rare Wave of Gratitude', 'Felt a wave of gratitude today. For people, for moments, for air. These moments are rare but they matter.', 'gratitude', 14, '2025-06-26 15:46:00'),
(106, 138, 'How Public Journaling Connects Strangers', 'Public journaling has helped me connect with strangers who feel exactly like me. It’s weirdly comforting.', 'vulnerability', 6, '2025-05-25 10:52:00'),
(107, 131, 'Sharing My Story for the First Time', 'Shared my story publicly for the first time. I was scared, but the support I got was overwhelming.', 'vulnerability', 11, '2025-07-03 14:40:00'),
(108, 145, 'A Sunrise That Felt Like a Fresh Start', 'I watched the sunrise today. Felt like the universe telling me to start fresh.', 'mindset', 40, '2025-06-30 00:32:00'),
(109, 127, 'Growth Is Not Linear?And That?s Okay', 'My therapist said growth is not linear. That sentence alone gave me permission to breathe.', 'healing', 9, '2025-06-19 10:00:00'),
(110, 104, 'Trying Meditation, Failing, and Trying Again', 'Tried meditation today. I suck at it. But weirdly, I want to try again tomorrow.', 'self-improvement', 4, '2025-08-17 15:42:49'),
(111, 136, 'Reconnecting With an Old Friend', 'Talked to an old friend today. No awkwardness. Just pure nostalgia and comfort.', 'friendship', 21, '2025-06-29 12:42:00'),
(112, 141, 'Letting Myself Rest Without Guilt', 'Today I didn’t feel like doing anything. And I let myself be okay with that.', 'acceptance', 5, '2025-07-02 08:08:00'),
(113, 134, 'Learning Sensitivity Is Strength', 'Was told I’m too sensitive again. But I’m learning that sensitivity is not weakness.', 'self-worth', 10, '2025-07-07 06:25:00'),
(114, 140, 'Doing Something Spontaneous Like a Child', 'Did something spontaneous today. It felt like I was 10 again. Free and unbothered.', 'inner-child', 16, '2025-06-28 12:00:00'),
(115, 129, 'The Deep Need to Be Understood', 'It’s scary how much we all want to be understood.', 'connection', 13, '2025-08-17 13:28:17'),
(116, 137, 'Lonely in a Room Full of People', 'Felt lonely in a room full of people. That’s a different kind of empty.', 'isolation', 13, '2025-07-08 13:35:00'),
(117, 130, 'Forgiving Myself After Years', 'Today I forgave myself for something I’ve been holding onto for years.', 'forgiveness', 15, '2025-06-20 08:50:00'),
(118, 143, 'Writing Letters I?ll Never Send', 'Started writing letters I’ll never send. It helps more than I thought.', 'coping', 7, '2025-07-09 04:20:00'),
(119, 126, 'Quiet Moments Before the Alarm', 'Woke up before my alarm today. Just lay there, thinking. Sometimes silence says more.', 'introspection', 4, '2025-06-22 02:33:00'),
(120, 144, 'Not Every Feeling Needs to Be Fixed', 'Was reminded that not every feeling needs to be fixed. Some just need to be felt.', 'emotional-awareness', 18, '2025-07-11 06:58:00'),
(121, 117, 'Healing Means the Damage No Longer Controls Me', 'Healing doesn’t mean the damage never existed. It means the damage no longer controls you.', 'healing', 20, '2025-06-18 10:00:00'),
(122, 108, 'A Day Offline Felt Like a Win', 'Spent the day offline. Noticed how much more present I felt. Might do this more often.', 'digital-detox', 6, '2025-07-05 11:30:00'),
(123, 112, 'Small Wins That Feel Like Mountains', 'Had a small win today. Nothing big. But it felt like a mountain moved.', 'growth', 11, '2025-07-04 03:40:00'),
(124, 118, 'Remembering Childhood Dreams', 'Remembered a dream I had as a kid. Might try chasing it again.', 'hope', 5, '2025-06-17 16:15:00'),
(125, 120, 'Progress Is Messy, but It?s Progress', 'I’m not where I want to be, but I’m not where I was either. Progress is messy.', 'progress', 9, '2025-07-06 02:45:00'),
(126, 105, 'Old songs can bring back memories we didn?t know we missed', 'Listened to an old song that brought back memories I didn’t know I missed.', 'nostalgia', 10, '2025-06-16 13:52:00'),
(127, 139, 'I don?t have it all figured out, but I?m learning to trust the process', 'I don’t have it all figured out. But I’m learning to trust the process.', 'patience', 17, '2025-07-07 15:14:00'),
(128, 142, 'The right words can make you feel seen when you need it most', 'Read something that made me feel seen today. Words are powerful.', 'literature', 18, '2025-08-21 07:14:39'),
(129, 124, 'Small things like water, walks, and rest matter more than we realize', 'Drank water. Took a walk. Took a nap. Small things help more than we give them credit.', 'self-care', 8, '2025-06-14 10:25:00'),
(130, 110, 'I?ve stopped waiting for permission to live fully', 'Realized I’ve been waiting for permission to live fully. I’m done waiting.', 'self-empowerment', 14, '2025-07-01 06:00:00'),
(131, 119, 'Breaking down and picking yourself up again is strength too', 'Broke down today. Then picked myself back up. That’s strength too.', 'resilience', 19, '2025-06-13 06:48:00'),
(132, 111, 'A stranger?s compliment can brighten an entire week', 'A stranger complimented me today. Made my entire week.', 'kindness', 9, '2025-08-21 07:49:37'),
(133, 116, 'Saying no to things that drain you feels weird?but powerful', 'Started saying no to things that drain me. Feels weird. Also powerful.', 'boundaries', 22, '2025-06-11 05:15:00'),
(134, 106, 'Journaling again can make your mind feel lighter', 'Wrote in my journal for the first time in weeks. My mind feels lighter.', 'journaling', 6, '2025-07-02 08:35:00'),
(135, 109, 'Ten minutes of silence without a phone can reset your soul', 'Sat in silence for 10 minutes. No phone. No noise. Just me. I needed that.', 'presence', 10, '2025-08-17 13:28:50'),
(136, 122, 'Getting lost sometimes helps you discover hidden places', 'Got lost on a walk and discovered a quiet spot I never knew existed.', 'serendipity', 15, '2025-07-03 07:45:00'),
(137, 128, 'Even when I don?t feel like trying, showing up still counts', 'Some days I don’t feel like trying. But I still show up. That counts.', 'effort', 11, '2025-07-09 11:20:00'),
(138, 103, 'A child?s laughter reminded me of joy without reason', 'Heard a child laugh today. Reminded me of joy without reason.', 'simplicity', 12, '2025-07-10 05:00:00'),
(139, 113, 'Deleting apps can feel like making space for yourself', 'I deleted some apps today. Making space feels good.', 'decluttering', 5, '2025-06-09 14:30:00'),
(140, 102, 'Writing a letter from your future self can be unexpectedly kind', 'Wrote myself a letter from the future. It was kinder than I expected.', 'self-compassion', 10, '2025-06-08 07:52:00'),
(142, 124, 'Doing something kind in silence is still worth it', 'Did something kind for someone today. No one saw it. Still worth it.', 'kindness', 6, '2025-06-06 10:25:00'),
(143, 109, 'Starting therapy showed me how much I was holding in', 'Started therapy last week. Didn’t know I was holding so much in.', 'healing', 13, '2025-06-05 08:40:00'),
(144, 132, 'Telling someone you appreciate them feels like a hug in words', 'Told someone I appreciate them today. Felt like a hug in words.', 'gratitude', 7, '2025-06-04 05:52:00'),
(145, 115, 'Being honest about how I feel made life feel real', 'Was honest today when someone asked how I was. It felt real.', 'authenticity', 16, '2025-06-03 05:15:00'),
(146, 117, 'Sometimes closure comes from within, not from others', 'Learned that sometimes closure comes from within, not from others.', 'self-awareness', 19, '2025-06-23 06:45:00'),
(147, 123, 'Reading before bed instead of scrolling changes everything', 'Started reading again before bed instead of scrolling. Sleep feels different now.', 'habits', 6, '2025-06-25 16:39:00'),
(148, 135, 'Hearing ?I?m proud of you? can mean more than you realize', 'Someone said they’re proud of me. I didn’t know I needed to hear that so badly.', 'validation', 14, '2025-07-04 11:17:00'),
(149, 132, 'Crying isn?t always sadness?it can be fullness too', 'I cried today. Not because I was sad. Just… full. Full of something I can’t explain.', 'emotions', 4, '2025-07-05 05:35:00'),
(150, 115, 'Posting without editing feels vulnerable, but also freeing', 'Posted a photo of myself without editing. Felt vulnerable. Also freeing.', 'self-love', 17, '2025-06-27 13:26:00'),
(151, 151, '', '', '', 0, '2025-08-22 11:03:22');

-- --------------------------------------------------------

--
-- Table structure for table `quotes`
--

CREATE TABLE `quotes` (
  `quote_id` int(11) NOT NULL,
  `quote` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quotes`
--

INSERT INTO `quotes` (`quote_id`, `quote`) VALUES
(1, 'Believe in yourself.'),
(2, 'Stay positive, work hard, make it happen.'),
(3, 'Success is not final, failure is not fatal.'),
(4, 'Dream big and dare to fail.'),
(5, 'Turn your wounds into wisdom.'),
(6, 'Happiness depends upon ourselves.'),
(7, 'Do what you can with what you have.'),
(8, 'Act as if what you do makes a difference. It does.'),
(9, 'Success usually comes to those who are too busy to be looking for it.'),
(10, 'Don\'t watch the clock; do what it does. Keep going.'),
(11, 'The best way to get started is to quit talking and begin doing.'),
(12, 'Don\'t wait for opportunity. Create it.'),
(13, 'Great things never come from comfort zones.'),
(14, 'Push yourself, because no one else is going to do it for you.'),
(15, 'Dream it. Wish it. Do it.'),
(16, 'Success doesn\'t just find you. You have to go out and get it.'),
(17, 'The harder you work for something, the greater you\'ll feel when you achieve it.'),
(18, 'Dream bigger. Do bigger.'),
(19, 'Don\'t stop when you\'re tired. Stop when you\'re done.'),
(20, 'Wake up with determination. Go to bed with satisfaction.'),
(21, 'Do something today that your future self will thank you for.'),
(22, 'Little things make big days.'),
(23, 'It\'s going to be hard, but hard does not mean impossible.'),
(24, 'Don\'t wait. The time will never be just right.'),
(25, 'The key to success is to focus on goals, not obstacles.'),
(26, 'Dreams don\'t work unless you do.'),
(27, 'Keep going. Everything you need will come to you at the perfect time.'),
(28, 'Doubt kills more dreams than failure ever will.'),
(29, 'Don\'t be afraid to give up the good to go for the great.'),
(30, 'Hard work beats talent when talent doesn\'t work hard.'),
(31, 'Opportunities don\'t happen, you create them.'),
(32, 'Success is what happens after you have survived your mistakes.'),
(33, 'Your limitation—it\'s only your imagination.'),
(34, 'Push harder than yesterday if you want a different tomorrow.'),
(35, 'Sometimes we\'re tested not to show our weaknesses, but to discover our strengths.'),
(36, 'The only place where success comes before work is in the dictionary.'),
(37, 'The future depends on what you do today.'),
(38, 'Don\'t be pushed around by the fears in your mind.'),
(39, 'Work while they sleep. Learn while they party. Save while they spend. Live like they dream.'),
(40, 'Great things take time.'),
(41, 'Success is not for the lazy.'),
(42, 'Be stronger than your excuses.'),
(43, 'Discipline is the bridge between goals and accomplishment.'),
(44, 'Your future is created by what you do today, not tomorrow.'),
(45, 'Don\'t limit your challenges. Challenge your limits.'),
(46, 'The way to get started is to quit talking and begin doing.'),
(47, 'Sometimes later becomes never. Do it now.'),
(48, 'The secret of getting ahead is getting started.'),
(49, 'If you get tired, learn to rest, not quit.'),
(50, 'Difficulties in life don\'t come to destroy you, but to help you realize your hidden potential.'),
(51, 'Failure is simply the opportunity to begin again, this time more intelligently.'),
(52, 'Don\'t let yesterday take up too much of today.'),
(53, 'Do what you love and you\'ll never work a day in your life.'),
(54, 'Strive for progress, not perfection.'),
(55, 'Work hard in silence, let success make the noise.'),
(56, 'Don\'t fear failure. Fear being in the exact same place next year as you are today.'),
(57, 'Difficult roads often lead to beautiful destinations.'),
(58, 'Don\'t count the days, make the days count.'),
(59, 'Do it with passion or not at all.'),
(60, 'Believe you can and you\'re halfway there.'),
(61, 'Failure will never overtake me if my determination to succeed is strong enough.'),
(62, 'We may encounter many defeats but we must not be defeated.'),
(63, 'Knowing is not enough; we must apply. Wishing is not enough; we must do.'),
(64, 'Imagine your life is perfect in every respect; what would it look like?'),
(65, 'Whether you think you can or think you can\'t, you\'re right.'),
(66, 'Security is mostly a superstition. Life is either a daring adventure or nothing.'),
(67, 'The man who has confidence in himself gains the confidence of others.'),
(68, 'Creativity is intelligence having fun.'),
(69, 'What you lack in talent can be made up with desire, hustle, and giving 110% all the time.'),
(70, 'Do what you can with all you have, wherever you are.'),
(71, 'Develop an ‘Attitude of Gratitude\'. Say thank you to everyone you meet.'),
(72, 'You are never too old to set another goal or to dream a new dream.'),
(73, 'Reading is to the mind, as exercise is to the body.'),
(74, 'Fake it until you make it! Act as if you had all the confidence you require until it becomes your reality.'),
(75, 'The only limit to our realization of tomorrow will be our doubts of today.'),
(76, 'The purpose of our lives is to be happy.'),
(77, 'Life is what happens when you\'re busy making other plans.'),
(78, 'Get busy living or get busy dying.'),
(79, 'You only live once, but if you do it right, once is enough.'),
(80, 'Many of life\'s failures are people who did not realize how close they were to success when they gave up.'),
(81, 'If you want to live a happy life, tie it to a goal, not to people or things.'),
(82, 'Never let the fear of striking out keep you from playing the game.'),
(83, 'Money and success don\'t change people; they merely amplify what is already there.'),
(84, 'Your time is limited, so don\'t waste it living someone else\'s life.'),
(85, 'Not how long, but how well you have lived is the main thing.'),
(86, 'If life were predictable it would cease to be life, and be without flavor.'),
(87, 'In order to write about life first you must live it.'),
(88, 'The big lesson in life, baby, is never be scared of anyone or anything.'),
(89, 'Curiosity about life in all of its aspects, I think, is still the secret of great creative people.'),
(90, 'Life is not a problem to be solved, but a reality to be experienced.'),
(91, 'Turn your wounds into wisdom.'),
(92, 'The unexamined life is not worth living.'),
(93, 'To live is the rarest thing in the world. Most people exist, that is all.'),
(94, 'Good friends, good books, and a sleepy conscience: this is the ideal life.'),
(95, 'Life is really simple, but we insist on making it complicated.'),
(96, 'Life is a succession of lessons which must be lived to be understood.'),
(97, 'Life is made of ever so many partings welded together.'),
(98, 'Your time is limited, so don\'t waste it living someone else\'s life.'),
(99, 'Believe in yourself.'),
(100, 'Stay positive, work hard, make it happen.');

-- --------------------------------------------------------

--
-- Table structure for table `to_do_list`
--

CREATE TABLE `to_do_list` (
  `ID` int(30) NOT NULL,
  `User_id` int(11) NOT NULL,
  `Title` varchar(30) NOT NULL,
  `List` varchar(1000) DEFAULT NULL,
  `completed_tasks` varchar(1000) DEFAULT NULL,
  `Date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `to_do_list`
--

INSERT INTO `to_do_list` (`ID`, `User_id`, `Title`, `List`, `completed_tasks`, `Date_time`) VALUES
(5, 151, 'checking', NULL, '', '2025-08-18 04:38:29'),
(6, 151, 'checking', NULL, '', '2025-08-18 04:38:46'),
(7, 151, 'checkingggg', '3. jhdbewjbgd\n', '', '2025-08-18 04:38:58'),
(9, 154, 'code checking', '1. xyz\n3. bjn\n', '2. abc1\n3. nnn\n', '2025-08-18 04:39:08'),
(10, 154, 'quote check', '2. D\n3. E\n', '3. c\n2. b\n1. a\n4. f\n', '2025-08-18 04:53:02'),
(13, 151, 'heyyy', '2. b\n3. e\n4. dd\n', '1. a\n', '2025-08-20 14:54:15');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(60) NOT NULL,
  `passcode` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `passcode`) VALUES
(101, 'woodsdaniel', 'Woodsdaniel#02'),
(102, 'wayne35', 'wayne352004'),
(103, 'flindsey', '21052001fli'),
(104, 'garydominguez', 'Garydominguez#10'),
(105, 'charles84', 'charles84@06'),
(106, 'zjohnson', 'zjohnson1996'),
(107, 'gibsontroy', 'Gibsontroy#07'),
(108, 'kdean', 'kdean@01'),
(109, 'wnolan', 'wnolan@30'),
(110, 'pwilson', 'pwilson1997'),
(111, 'matthewaustin', 'Matthewaustin#04'),
(112, 'bsullivan', 'Bsullivan#07'),
(113, 'saraperry', 'saraperry@26'),
(114, 'pennyfarrell', '16121988pen'),
(115, 'bradleyrebecca', 'bradleyrebecca1993'),
(116, 'alexanderfischer', 'alexanderfischer@01'),
(117, 'ophillips', 'Ophillips#01'),
(118, 'hbecker', 'Hbecker#08'),
(119, 'ascott', 'Ascott#08'),
(120, 'brittney25', 'Brittney25#06'),
(121, 'terryjoshua', '02121987ter'),
(122, 'kathyellis', 'Kathyellis#11'),
(123, 'fgriffin', '24101999fgr'),
(124, 'fwalton', 'fwalton1995'),
(125, 'carolyn55', 'carolyn55@10'),
(126, 'thomas46', 'thomas461996'),
(127, 'william08', 'william082000'),
(128, 'steven59', '12041997ste'),
(129, 'patrick00', 'Patrick00#10'),
(130, 'cervantesvanessa', 'Cervantesvanessa#10'),
(131, 'williamstamara', 'williamstamara@03'),
(132, 'thomas30', 'thomas301991'),
(133, 'lisahinton', 'lisahinton@08'),
(134, 'imann', 'imann1985'),
(135, 'smithsamantha', 'smithsamantha@07'),
(136, 'markcoleman', 'markcoleman1997'),
(137, 'jalexander', 'Jalexander#01'),
(138, 'fergusoncraig', 'fergusoncraig@06'),
(139, 'tranjessica', '05111996tra'),
(140, 'cdawson', 'cdawson@22'),
(141, 'cynthiaschultz', 'cynthiaschultz1988'),
(142, 'lindseyhumphrey', 'Lindseyhumphrey#12'),
(143, 'robertramirez', 'robertramirez@11'),
(144, 'nicholetorres', 'Nicholetorres#03'),
(145, 'hillkarina', 'hillkarina2001'),
(146, 'yolsen', 'Yolsen#07'),
(147, 'nmiller', 'nmiller@13'),
(148, 'bwall', 'bwall@13'),
(149, 'erica82', 'erica822001'),
(150, 'bturner', '16041996btu'),
(151, 'xyzzz', '12344321'),
(152, 'abccc', '12345678'),
(153, 'xyzabc', '12345678'),
(154, 'vvvv', '09876543'),
(155, 'dakshgadhiya13', 'daksh1173');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `challenges_table`
--
ALTER TABLE `challenges_table`
  ADD PRIMARY KEY (`challenge_ID`) USING BTREE,
  ADD UNIQUE KEY `unique_challenges` (`challenge_desc`) USING HASH,
  ADD KEY `fk_challenge_user_id` (`user_ID`);

--
-- Indexes for table `journal`
--
ALTER TABLE `journal`
  ADD KEY `fk_journal_user` (`user_id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`post_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `to_do_list`
--
ALTER TABLE `to_do_list`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fk_userId` (`User_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `challenges_table`
--
ALTER TABLE `challenges_table`
  MODIFY `challenge_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `to_do_list`
--
ALTER TABLE `to_do_list`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `challenges_table`
--
ALTER TABLE `challenges_table`
  ADD CONSTRAINT `fk_challenge_user_id` FOREIGN KEY (`user_ID`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `journal`
--
ALTER TABLE `journal`
  ADD CONSTRAINT `fk_journal_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `to_do_list`
--
ALTER TABLE `to_do_list`
  ADD CONSTRAINT `fk_userId` FOREIGN KEY (`User_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
