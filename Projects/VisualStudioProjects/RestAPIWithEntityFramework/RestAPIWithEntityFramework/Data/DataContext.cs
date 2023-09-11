using System;
using Microsoft.EntityFrameworkCore;

namespace RestAPIWithEntityFramework.Data
{
	public class DataContext : DbContext
	{
		public DataContext(DbContextOptions<DataContext> options) : base(options)
		{
		}

		public DbSet<SuperHero> SuperHeroes { get; set; }
	}
}

