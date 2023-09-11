using System;
namespace RestAPIWithEntityFramework
{
	public class SuperHero
	{
		public SuperHero()
		{
		}
        public int Id { get; set; }
		public string name { get; set; } = string.Empty;
		public string firstName { get; set; } = string.Empty;
		public string lastName { get; set; } = string.Empty;
		public string Place { get; set; } = string.Empty;
    }
}

