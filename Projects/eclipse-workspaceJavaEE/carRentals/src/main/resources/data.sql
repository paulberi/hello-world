
INSERT INTO User (Address, Name, Password, Role, User_name,enabled) VALUES
  ('Tråggränd 11','Paul','password1','Admin', 'username1','true'),
  ('Tråggränd 12','Beri','password2','Admin', 'username2','true'),
  ('Tråggränd 13','Lukong','password3','Customer', 'username3','true'),
  ('Tråggränd 14','Nso','password4','Customer', 'username4','true');


  
INSERT INTO Car (Model, Model_Type, Passengers, Price_per_day) VALUES
  ('Toyota','LandCruiser', 7,  5500),
  ('Mercedes','Sprinter',3, 2000),
  ('Mitsubishi','Outlander',4, 2000),
  ('Volvo', 'V60', 4, 3500);

  
INSERT INTO roles (role_name, role_desc) VALUES
  ('Admin','super user'),
  ('Customer','basic user');
  
  
INSERT INTO user_roles (User_id, role_id) VALUES
  (1,1),
  (2,1),
  (3,2),
  (4,2);
  